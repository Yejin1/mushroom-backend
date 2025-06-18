package dev.yejin1.mushroom_backend.sale.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalesReportedEvent {
    private Long usrId;
    private Integer amount;
    private String timestamp;
}
