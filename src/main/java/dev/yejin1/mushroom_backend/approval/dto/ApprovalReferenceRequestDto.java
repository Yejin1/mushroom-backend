package dev.yejin1.mushroom_backend.approval.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApprovalReferenceRequestDto {
    private Long docId;
    private List<Long> refUsrIds;   // 개인 참조자
    private List<Long> refDeptIds;  // 부서 참조자
}
