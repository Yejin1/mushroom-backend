package dev.yejin1.mushroom_backend.approval.entity;

import dev.yejin1.mushroom_backend.approval.dto.ApprovalStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "approval_line")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApprovalLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 결재문서 연관관계 (N:1)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approval_doc_id", nullable = false)
    private ApprovalDoc approvalDoc;

    @Column(nullable = false)
    private Long approverId;

    @Column(nullable = false, length = 50)
    private String approverName;

    @Column(length = 50)
    private String approverPosition;

    @Column(length = 50)
    private String approverDepartment;

    @Column(nullable = false)
    private int stepOrder;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ApprovalStatus status; // WRITING, WAITING, APPROVED, REJECTED

    @Builder.Default
    @Column(nullable = false)
    private boolean isFinalApprover = false;

    @Builder.Default
    @Column(nullable = false)
    private boolean isParallel = false;

    @Column(length = 500)
    private String comment;  // 결재 의견

    private LocalDateTime approvedAt; // 실제 승인 시각

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime statusUpdatedAt;
}
