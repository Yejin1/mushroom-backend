package dev.yejin1.mushroom_backend.common.outbox;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.yejin1.mushroom_backend.common.notify.SlackNotifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Component
public class OutboxDispatcher {

    private final OutboxRepository outboxRepository;
    private final SlackNotifier slackNotifier;
    private final ObjectMapper om = new ObjectMapper();

    public OutboxDispatcher(OutboxRepository outboxRepository, SlackNotifier slackNotifier) {
        this.outboxRepository = outboxRepository;
        this.slackNotifier = slackNotifier;
    }

    @Value("${outbox.dispatch.batchSize:20}")
    private int batchSize;

    @Scheduled(fixedDelayString = "${outbox.dispatch.fixedDelayMs:30000}")
    @Transactional
    public void dispatch() {
        List<OutboxEvent> batch = outboxRepository.pickPendingForDispatch(Instant.now(), PageRequest.of(0, batchSize));
        for (OutboxEvent o : batch) {
            try {
                Map<String, String> payload = om.readValue(o.getPayloadJson(), new TypeReference<>() {});
                switch (o.getEventType()) {
                    case "APPROVAL_COMPLETED" -> {
                        slackNotifier.send(
                                payload.get("title"),
                                payload.get("approver"),
                                payload.get("link")
                        );
                        o.markSent();
                    }
                    default -> o.markSent(); // 모르는 이벤트는 소비 처리
                }
            } catch (Exception ex) {
                o.backoff(); // 실패 → 지수 백오프 후 재시도
            }
        }
    }
}
