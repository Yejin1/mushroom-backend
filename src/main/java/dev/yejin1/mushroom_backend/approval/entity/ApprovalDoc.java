/**
 * ApprovalDoc
 *
 * 전자결재 문서정보 테이블 Entity
 *
 * <p>
 *     전자결재 문서 기본 정보
 *     목록 조회 시 사용
 * </p>
 *
 * @author Yejin1
 * @since 2025-05-17
 */
package dev.yejin1.mushroom_backend.approval.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "approval_doc")
@Data
public class ApprovalDoc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "DOC_NO", length = 50, unique = true)
    private String docNo;

    @Column(name = "FORM_ID")
    private Long formId;

    @Column(name = "FORM_NM", length = 100)
    private String formNm;

    @Column(name = "TITLE", nullable = false, length = 200)
    private String title;

    @Column(name = "WRITER", nullable = false)
    private Long writer;

    @Column(name = "WRITER_NM", length = 50)
    private String writerNm;

    @Column(name = "CREATED_DT", nullable = false)
    private LocalDateTime createdDt;

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

}
