/**
 * ApprovalDocResponseDto
 *
 * 전자결재 문서 목록 조회용 Dto
 *
 * <p>
 * </p>
 *
 * @author Yejin1
 * @since 2025-05-17
 */
package dev.yejin1.mushroom_backend.approval.dto;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ApprovalDocResponseDto {
    private Long id;
    private String docNo;
    private Long formId;
    private String formNm;
    private String title;
    private Long writer;
    private String writerNm;
    private Integer statusCd;
    private String statusNm;
    private Long currentUsr;
    private LocalDateTime createdDt;
    private LocalDateTime completedDt;
    private String urgentYn;
}
