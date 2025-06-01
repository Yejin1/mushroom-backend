/**
 * ApprovalDocBody
 *
 * 전자결재 문서 내용 테이블 Entity
 *
 * <p>
 *     전자결재 내용테이블
 * </p>
 *
 * @author Yejin1
 * @since 2025-05-17
 */
package dev.yejin1.mushroom_backend.approval.entity;


import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "approval_doc_body")
@Data
public class ApprovalDocBody {

    @Id
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private ApprovalDoc doc;

    @Column(name = "FORM_CONTENT", columnDefinition = "json")
    private String formContent;

    @Lob
    @Column(name = "EDITOR_CONTENT", columnDefinition = "TEXT")
    private String editorContent;

    @Column(name = "EDITOR_YN", length = 1)
    private String editorYn;

    @Column(name = "LAST_EDITED_BY")
    private Long lastEditedBy;

    @Column(name = "LAST_EDITED_DT")
    private LocalDateTime lastEditedDt;
}