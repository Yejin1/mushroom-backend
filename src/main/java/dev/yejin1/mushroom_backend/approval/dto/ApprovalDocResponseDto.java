package dev.yejin1.mushroom_backend.approval.dto;


import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ApprovalDocResponseDto {
    private Long id;
    private String docNo;
    private Long formId;
    private String title;
    private Long writer;
    private Integer statusCd;
    private String statusNm;
    private Long currentUsr;
    private LocalDateTime createDt;
    private LocalDateTime completedDt;
    private String urgentYn;
}
