package dev.yejin1.mushroom_backend.sale.service;

import dev.yejin1.mushroom_backend.sale.dto.SalesReportedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class SalesReportProducer {

    private final KafkaTemplate<String, SalesReportedEvent> kafkaTemplate;

    public SalesReportProducer(KafkaTemplate<String, SalesReportedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(SalesReportedEvent event) {
        kafkaTemplate.send("sales.reported", event);
        System.out.println("🟢 Kafka 메시지 발행: " + event);
    }
}
