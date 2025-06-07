package dev.yejin1.mushroom_backend.approval.entity;

import dev.yejin1.mushroom_backend.org.entity.OrgDept;
import dev.yejin1.mushroom_backend.org.entity.OrgUsr;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "approval_reference")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApprovalReference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 결재 문서 연관
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approval_doc_id", nullable = false)
    private ApprovalDoc approvalDoc;

    // 참조자 (사용자)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ref_usr_id")
    private OrgUsr refUsr;

    // 참조 부서
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ref_dept_id")
    private OrgDept refDept;

    @Column(length = 50)
    private String refDeptName;

    // 참조자 이름 프론트 표출용 캐싱)
    @Column(length = 50)
    private String refUsrName;

    // USER 또는 DEPT
    @Column(name = "ref_type", nullable = false, length = 10)
    private String refType;

    @Column(nullable = false, length = 50)
    private String createdBy;

    @CreationTimestamp
    private LocalDateTime createdDt;
}
