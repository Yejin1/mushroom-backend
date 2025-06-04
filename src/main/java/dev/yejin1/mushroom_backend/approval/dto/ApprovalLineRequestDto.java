package dev.yejin1.mushroom_backend.approval.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApprovalLineRequestDto {
    private Long usrId;
    private String usrNm;
    private String posNm;
    private String deptNm;
    private int stepOrder;
}
