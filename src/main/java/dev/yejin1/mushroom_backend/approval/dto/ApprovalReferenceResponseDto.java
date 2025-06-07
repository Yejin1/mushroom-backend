package dev.yejin1.mushroom_backend.approval.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApprovalReferenceResponseDto {
    private List<UserRefDto> userRefs;
    private List<DeptRefDto> deptRefs;
}
