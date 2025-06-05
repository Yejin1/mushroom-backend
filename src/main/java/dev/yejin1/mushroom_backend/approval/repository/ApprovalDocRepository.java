/**
 * ApprovalDocRepository
 *
 * 전자결재 문서 테이블용 Repository
 *
 * <p>
 * </p>
 *
 * @author Yejin1
 * @since 2025-05-17
 */
package dev.yejin1.mushroom_backend.approval.repository;

import dev.yejin1.mushroom_backend.approval.dto.ApprovalDocResponseDto;
import dev.yejin1.mushroom_backend.approval.entity.ApprovalDoc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ApprovalDocRepository  extends JpaRepository<ApprovalDoc, Long> {

    List<ApprovalDoc> findByStatusCd(Integer statusCd);

    List<ApprovalDoc> findByWriter(Long writer);

    List<ApprovalDoc> findByStatusCdAndUrgentYn(Integer statusCd, String urgentYn);

    @Query("""
            SELECT new dev.yejin1.mushroom_backend.approval.dto.ApprovalDocResponseDto(
            id,
            null as lineId,
            writer as userId,
            docNo,
            formId,
            formNm,
            title,
            writer,
            writerNm,
            statusCd,
            statusNm,
            currentUsr,
            createdDt,
            completedDt,
            urgentYn
            )
            FROM ApprovalDoc 
            WHERE writer = :writer
              AND statusCd = :statusCd
            """)
    Page<ApprovalDocResponseDto> findApprovalDocsByConditions(
            @Param("writer") Long writer,
            @Param("statusCd")Integer statusCd,
            Pageable pageable
    );


    //****** 결재함별 문서 조회 쿼리

    //1. 결재함(결재해야하는 문서)
    @Query("""
    SELECT new dev.yejin1.mushroom_backend.approval.dto.ApprovalDocResponseDto(
        d.id,
        l.id as lineId,
        l.approverId as userId,
        d.docNo,
        d.formId,
        d.formNm,
        d.title,
        d.writer,
        d.writerNm,
        d.statusCd,
        d.statusNm,
        d.currentUsr,
        d.createdDt,
        d.completedDt,
        d.urgentYn
    )
    FROM ApprovalDoc d
    LEFT JOIN ApprovalLine l ON l.approvalDoc.id = d.id
    WHERE (l.approverId = :usrId AND l.status = 'WAITING')
       OR d.currentUsr = :usrId
""")
    Page<ApprovalDocResponseDto> findMyApprovalBox(@Param("usrId") Long usrId, Pageable pageable);


    //2. 진행함 :결재라인에 내가 있으면서, 현재 미완인 문서
    @Query("""
    SELECT DISTINCT new dev.yejin1.mushroom_backend.approval.dto.ApprovalDocResponseDto(
        d.id,
        l.id as lineId,
        l.approverId as userId,
        d.docNo,
        d.formId,
        d.formNm,
        d.title,
        d.writer,
        d.writerNm,
        d.statusCd,
        d.statusNm,
        d.currentUsr,
        d.createdDt,
        d.completedDt,
        d.urgentYn
    )
    FROM ApprovalDoc d
    JOIN ApprovalLine l ON l.approvalDoc.id = d.id
    WHERE l.approverId = :usrId
      AND d.statusCd = 1
""")
    Page<ApprovalDocResponseDto> findMyProcessingBox(@Param("usrId") Long usrId, Pageable pageable);


    //3. 완료함 - approvalLine에 내가 있으면서, 완료된 문서
    @Query("""
    SELECT new dev.yejin1.mushroom_backend.approval.dto.ApprovalDocResponseDto(
        d.id,
        l.id as lineId,
        l.approverId as userId,
        d.docNo,
        d.formId,
        d.formNm,
        d.title,
        d.writer,
        d.writerNm,
        d.statusCd,
        d.statusNm,
        d.currentUsr,
        d.createdDt,
        d.completedDt,
        d.urgentYn
    )
    FROM ApprovalDoc d
    JOIN ApprovalLine l ON l.approvalDoc.id = d.id
    WHERE l.approverId = :usrId
      AND l.status = 'APPROVED'
      AND d.statusCd = 2
""")
    Page<ApprovalDocResponseDto> findMyCompletedBox(@Param("usrId") Long usrId, Pageable pageable);

    //4. 반려함 : 작성자가 나이면서, 반려되거나 회수된 문서
    @Query("""
    SELECT new dev.yejin1.mushroom_backend.approval.dto.ApprovalDocResponseDto(
        d.id,
        null as lineId,
        d.writer as userId,
        d.docNo,
        d.formId,
        d.formNm,
        d.title,
        d.writer,
        d.writerNm,
        d.statusCd,
        d.statusNm,
        d.currentUsr,
        d.createdDt,
        d.completedDt,
        d.urgentYn
    )
    FROM ApprovalDoc d
    WHERE d.writer = :usrId
      AND d.statusCd IN (3, 4) 
""")
    Page<ApprovalDocResponseDto> findMyRejectedBox(@Param("usrId") Long usrId, Pageable pageable);


    //5. 참조함
    @Query("""
    SELECT new dev.yejin1.mushroom_backend.approval.dto.ApprovalDocResponseDto(
        d.id,
        null as lineId,
        r.refUsr.usrId as userId,
        d.docNo,
        d.formId,
        d.formNm,
        d.title,
        d.writer,
        d.writerNm,
        d.statusCd,
        d.statusNm,
        d.currentUsr,
        d.createdDt,
        d.completedDt,
        d.urgentYn
    )
    FROM ApprovalReference r
    JOIN r.approvalDoc d
    WHERE r.refUsr.usrId = :usrId
""")
    Page<ApprovalDocResponseDto> findMyReferenceBox(@Param("usrId") Long usrId, Pageable pageable);

    //6. 부서결재함 - 완료함
    @Query("""
    SELECT new dev.yejin1.mushroom_backend.approval.dto.ApprovalDocResponseDto(
        d.id,
        null as lineId,
        null as userId,
        d.docNo,
        d.formId,
        d.formNm,
        d.title,
        d.writer,
        d.writerNm,
        d.statusCd,
        d.statusNm,
        d.currentUsr,
        d.createdDt,
        d.completedDt,
        d.urgentYn
    )
    FROM ApprovalDoc d
    JOIN OrgUsr u ON d.writer = u.usrId
    WHERE u.dept.deptId = :deptId
      AND d.statusCd = 2
""")
    Page<ApprovalDocResponseDto> findDeptCompletedBox(@Param("deptId") Long deptId, Pageable pageable);

    //7. 부서결재함 - 참조함
    @Query("""
    SELECT new dev.yejin1.mushroom_backend.approval.dto.ApprovalDocResponseDto(
        d.id,
        null as lineId,
        null as userId,
        d.docNo,
        d.formId,
        d.formNm,
        d.title,
        d.writer,
        d.writerNm,
        d.statusCd,
        d.statusNm,
        d.currentUsr,
        d.createdDt,
        d.completedDt,
        d.urgentYn
    )
    FROM ApprovalReference r
    JOIN r.approvalDoc d
    WHERE r.refDept.deptId = :deptId
""")
    Page<ApprovalDocResponseDto> findDeptReferenceBox(@Param("deptId") Long deptId, Pageable pageable);


}
