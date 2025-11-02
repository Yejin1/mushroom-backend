package dev.yejin1.mushroom_backend.common.notify;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Component
public class SlackNotifier {

    private final WebClient webClient = WebClient.builder().build();

    @Value("${notify.slack.webhook:}")
    private String webhook; // 비어있으면 로컬/테스트에서 전송 건너뜀

    public void send(String title, String approver, String link) {
        if (webhook == null || webhook.isBlank())
            return;
        String text = "*결재 완료* : " + title;
        webClient.post()
                .uri(webhook)
                .bodyValue(Map.of("text", text))
                .retrieve()
                .toBodilessEntity()
                .block();
    }
}
