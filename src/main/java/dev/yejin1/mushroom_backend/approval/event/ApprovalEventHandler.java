package dev.yejin1.mushroom_backend.approval.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.yejin1.mushroom_backend.common.outbox.OutboxEvent;
import dev.yejin1.mushroom_backend.common.outbox.OutboxRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;

import java.util.Map;

@Component
public class ApprovalEventHandler {

    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ApprovalEventHandler(OutboxRepository outboxRepository) {
        this.outboxRepository = outboxRepository;
    }

    // 트랜잭션 커밋 이후에만 Outbox 적재
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void onApprovalCompleted(ApprovalCompletedEvent e) {
        // 멱등키가 있으면 중복 방지
        if (e.uniqueKey() != null && outboxRepository.findByUniqueKey(e.uniqueKey()).isPresent()) {
            return;
        }

        var payload = Map.of(
                "title", e.title(),
                "approver", e.approverName(),
                "link", e.link());
        try {
            String payloadJson = objectMapper.writeValueAsString(payload);
            var outbox = OutboxEvent.pending(
                    "APPROVAL_COMPLETED",
                    e.docId(),
                    payloadJson,
                    e.uniqueKey());
            outboxRepository.save(outbox);
        } catch (JsonProcessingException ex) {
            throw new IllegalStateException("Outbox payload serialize 실패", ex);
        }
    }
}
