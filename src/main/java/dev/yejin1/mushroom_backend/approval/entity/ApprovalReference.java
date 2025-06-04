package dev.yejin1.mushroom_backend.approval.entity;
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

    // 결재 문서와 연관
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approval_doc_id", nullable = false)
    private ApprovalDoc approvalDoc;

    // 참조자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ref_usr_id", nullable = false)
    private OrgUsr refUsr;

    @Column(nullable = false, length = 50)
    private String refUsrName;

    @Column(nullable = false, length = 50)
    private String createdBy;

    @CreationTimestamp
    private LocalDateTime createdDt;
}

