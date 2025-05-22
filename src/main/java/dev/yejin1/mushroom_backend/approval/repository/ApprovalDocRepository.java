package dev.yejin1.mushroom_backend.approval.repository;

import dev.yejin1.mushroom_backend.approval.dto.ApprovalDocResponseDto;
import dev.yejin1.mushroom_backend.approval.entity.ApprovalDoc;
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
            docNo,
            formId,
            formNm,
            title,
            writer,
            writerNm,
            statusCd,
            statusNm,
            currentUsr,
            createDt,
            completedDt,
            urgentYn
            )
            FROM ApprovalDoc 
            WHERE writer = :writer
              AND statusCd = :statusCd
            """)
    List<ApprovalDocResponseDto> findApprovalDocsByConditions(
            @Param("writer") Long writer,
            @Param("statusCd")Integer statusCd
    );

}
