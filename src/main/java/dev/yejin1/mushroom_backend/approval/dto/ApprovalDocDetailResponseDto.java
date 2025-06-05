package dev.yejin1.mushroom_backend.approval.dto;

import dev.yejin1.mushroom_backend.approval.entity.ApprovalDocBody;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class ApprovalDocDetailResponseDto {
    private ApprovalDocBody body;
    private List<ApprovalLineDto> approvalLines;
}
