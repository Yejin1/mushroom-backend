package dev.yejin1.mushroom_backend.approval.repository;

import dev.yejin1.mushroom_backend.approval.entity.ApprovalDoc;
import dev.yejin1.mushroom_backend.approval.entity.ApprovalLine;
import dev.yejin1.mushroom_backend.approval.dto.ApprovalStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ApprovalLineRepository extends JpaRepository<ApprovalLine, Long> {

    // 문서 ID로 전체 결재선 조회
    List<ApprovalLine> findByApprovalDocOrderByStepOrderAsc(ApprovalDoc doc);

    // 특정 문서에서 특정 순서의 결재선 조회 (ex: 마지막 결재자 세팅용)
    Optional<ApprovalLine> findByApprovalDocAndStepOrder(ApprovalDoc doc, int stepOrder);

    // 특정 문서에서 대기 중인 결재자 조회
    Optional<ApprovalLine> findByApprovalDocAndStatus(ApprovalDoc doc, ApprovalStatus status);

    // 특정 문서에서 특정 결재자 조회
    Optional<ApprovalLine> findByApprovalDocAndApproverId(ApprovalDoc doc, Long approverId);
}
