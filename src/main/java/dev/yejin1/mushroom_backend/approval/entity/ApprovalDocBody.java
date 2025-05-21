package dev.yejin1.mushroom_backend.approval.entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
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

    @Column(name = "LAST_EDITED_BY")
    private Long lastEditedBy;

    @Column(name = "LAST_EDITED_DT")
    private LocalDateTime lastEditedDt;
}