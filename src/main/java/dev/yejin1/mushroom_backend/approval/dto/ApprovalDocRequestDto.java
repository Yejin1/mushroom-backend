package dev.yejin1.mushroom_backend.approval.dto;

import lombok.Data;
import lombok.Getter;

import java.util.Map;

@Data
@Getter
public class ApprovalDocRequestDto {
    private Long formId;
    private String title;
    private Long writer;
    private String urgentYn;
    private Map<String, Object> formContent;
}
