package dev.yejin1.mushroom_backend.sale.service;

import dev.yejin1.mushroom_backend.approval.dto.ApprovalDocRequestDto;
import dev.yejin1.mushroom_backend.approval.entity.ApprovalDoc;
import dev.yejin1.mushroom_backend.sale.dto.SalesReportedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SaleService {

    private final SalesReportProducer salesReportProducer;

    public void handleSaleDoc(ApprovalDocRequestDto dto) {
        long usrId = dto.getWriter();
        Integer amount = (Integer) dto.getFormContent().get("amount");

        handleSalesSubmitted(usrId,amount);


    }

    public void handleSalesSubmitted(Long usrId, Integer amount) {
        SalesReportedEvent event = new SalesReportedEvent(
                usrId,
                amount,
                LocalDateTime.now().toString()
        );
        salesReportProducer.send(event);
    }
}
