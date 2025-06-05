package dev.yejin1.mushroom_backend.approval.dto;

import dev.yejin1.mushroom_backend.approval.dto.ApprovalStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class ApprovalLineDto {
    private Long approverId;
    private String empNo;
    private String approverName;
    private String approverPosition;
    private String approverDepartment;
    private int stepOrder;
    private ApprovalStatus status;
    private boolean isFinalApprover;
    private String comment;
    private LocalDateTime approvedDt;
}
