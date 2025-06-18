package dev.yejin1.mushroom_backend.sale.service;

import dev.yejin1.mushroom_backend.sale.dto.SalesReportedEvent;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SaleService {

    private SalesReportProducer salesReportProducer;

    public void handleSalesSubmitted(Long usrId, Integer amount) {
        SalesReportedEvent event = new SalesReportedEvent(
                usrId,
                amount,
                LocalDateTime.now().toString()
        );
        salesReportProducer.send(event);
    }
}
