package dev.yejin1.mushroom_backend.approval.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "approval_doc")
@Data
@NoArgsConstructor
public class ApprovalDoc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "DOC_NO", length = 50, unique = true)
    private String docNo;

    @Column(name = "FORM_ID")
    private Long formId;

    @Column(name = "TITLE", nullable = false, length = 200)
    private String title;

    @Column(name = "WRITER", nullable = false)
    private Long writer;

    @Column(name = "CREATE_DT", nullable = false)
    private LocalDateTime createDt;

    @Column(name = "COMPLETED_DT")
    private LocalDateTime completedDt;

    @Column(name = "STATUS_CD", nullable = false)
    private Integer statusCd;

    @Column(name = "STATUS_NM", nullable = false, length = 20)
    private String statusNm;

    @Column(name = "CURRENT_USR")
    private Long currentUsr;

    @Column(name = "URGENT_YN", length = 1)
    private String urgentYn = "N";

    @OneToOne(mappedBy = "doc", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private ApprovalDocBody body;
}
