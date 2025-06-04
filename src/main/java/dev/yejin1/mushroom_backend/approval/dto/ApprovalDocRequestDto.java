/**
 * ApprovalDocRequestDto
 *
 * 전자결재 문서 작성용 Dto
 *
 * <p>
 * </p>
 *
 * @author Yejin1
 * @since 2025-05-17
 */
package dev.yejin1.mushroom_backend.approval.dto;

import lombok.Data;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Data
@Getter
public class ApprovalDocRequestDto {
    private Long formId;
    private String title;
    private Long writer;
    private String urgentYn;
    private String editorYn;
    private Map<String, Object> formContent;
    private String editorContent;
    private List<ApprovalLineRequestDto> approvalLine;

}
