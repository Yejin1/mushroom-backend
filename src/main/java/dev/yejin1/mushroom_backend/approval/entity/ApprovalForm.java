/**
 * ApprovalForm
 *
 * 전자결재 양식 테이블 Entity
 *
 * <p>
 * </p>
 *
 * @author Yejin1
 * @since 2025-05-17
 */
package dev.yejin1.mushroom_backend.approval.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "approval_form")
@Data
@NoArgsConstructor
public class ApprovalForm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NAME", nullable = false, length = 100)
    private String name;  // 예: "휴가 신청서"

    @Column(name = "SHORT_NAME", nullable = false, length = 30)
    private String shortName;  // 문서번호에 사용될 약명

    @Column(name = "CODE", nullable = false, unique = true, length = 50)
    private String code;  // 예: "VACATION", "EXPENSE", "BUSINESS_TRIP"

    @Column(name = "DESCRIPTION", length = 500)
    private String description;  // 양식 설명

    @Column(name = "FORM_SCHEMA", columnDefinition = "json")
    private String formSchema;  // 프론트가 렌더링할 JSON 구조

    @Column(name = "ACTIVE_YN", length = 1)
    private String activeYn = "Y";  // 사용 여부

    @Column(name = "CREATE_DT")
    private LocalDateTime createDt;

    @Column(name = "UPDATE_DT")
    private LocalDateTime updateDt;
}
