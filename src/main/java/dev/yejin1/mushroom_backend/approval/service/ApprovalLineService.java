package dev.yejin1.mushroom_backend.approval.service;


import dev.yejin1.mushroom_backend.approval.dto.ApprovalStatus;
import dev.yejin1.mushroom_backend.approval.entity.ApprovalDoc;
import dev.yejin1.mushroom_backend.approval.entity.ApprovalLine;
import dev.yejin1.mushroom_backend.approval.event.ApprovalCompletedEvent;
import dev.yejin1.mushroom_backend.approval.repository.ApprovalDocRepository;
import dev.yejin1.mushroom_backend.approval.repository.ApprovalLineRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ApprovalLineService {

    private final ApprovalLineRepository approvalLineRepository;
    private final ApprovalDocRepository approvalDocRepository;
    private final ApplicationEventPublisher publisher;

    @Transactional
    public void approve(Long lineId, Long currentUserId) {
        ApprovalLine line = approvalLineRepository.findById(lineId)
                .orElseThrow(() -> new RuntimeException("결재선 정보 없음"));

        // 1. 결재자 본인 확인
        if (!line.getApproverId().equals(currentUserId)) {
            throw new RuntimeException("결재자 본인이 아닙니다");
        }

        // 2. 현재 상태 확인
        if (line.getStatus() != ApprovalStatus.WAITING) {
            throw new RuntimeException("결재 대기 상태가 아닙니다");
        }

        // 3. 현재 결재자 승인 처리
        line.setStatus(ApprovalStatus.APPROVED);
        line.setApprovedDt(LocalDateTime.now());

        ApprovalDoc doc = line.getApprovalDoc();

        // 4. 다음 결재자 상태 WAITING으로 변경 (병렬 결재는 아직 없음)
        List<ApprovalLine> lines = approvalLineRepository.findByApprovalDocOrderByStepOrderAsc(doc);
        int currentOrder = line.getStepOrder();

        boolean hasNext = false;
        for (ApprovalLine l : lines) {
            if (l.getStepOrder() == currentOrder + 1) {
                l.setStatus(ApprovalStatus.WAITING);
                hasNext = true;
                break;
            }
        }

        // 5. 마지막 결재자였다면 문서 상태 변경
        if (!hasNext && line.isFinalApprover()) {
            doc.setStatusCd(2); // 예시: 2 = 승인완료
            doc.setStatusNm("결재완료");
            doc.setCompletedDt(LocalDateTime.now());
        }

        // 결재 알림용 이벤트 발행
        var event = new ApprovalCompletedEvent(
                doc.getId(), doc.getTitle(), "approver-"+currentUserId,
                "/docs/" + doc.getId(),
                "approve:" + doc.getId() // 멱등키(문서당 1회)
        );
        publisher.publishEvent(event);
    }

    @Transactional
    public void rejectApproval(Long lineId, Long currentUsrId, String comment) {
        ApprovalLine line = approvalLineRepository.findById(lineId)
                .orElseThrow(() -> new RuntimeException("결재선 없음"));

        if (!line.getStatus().equals(ApprovalStatus.WAITING)) {
            throw new RuntimeException("대기 중인 결재가 아닙니다");
        }

        if (!line.getApproverId().equals(currentUsrId)) {
            throw new RuntimeException("현재 결재자가 아닙니다");
        }

        // 반려 처리
        line.setStatus(ApprovalStatus.REJECTED);
        line.setComment(comment);
        line.setApprovedDt(LocalDateTime.now());
        line.setStatusUpdatedDt(LocalDateTime.now());
        approvalLineRepository.save(line);

        // 문서 상태도 반려로 변경
        ApprovalDoc doc = line.getApprovalDoc();
        doc.setStatusCd(3);
        doc.setStatusNm("반려");
        approvalDocRepository.save(doc);
    }

    @Transactional
    public void withdrawApproval(Long docId, Long currentUsrId) {
        ApprovalDoc doc = approvalDocRepository.findById(docId)
                .orElseThrow(() -> new RuntimeException("문서 없음"));

        if (!doc.getWriter().equals(currentUsrId)) {
            throw new RuntimeException("작성자만 회수할 수 있습니다");
        }

        if (doc.getStatusCd() != 0 && doc.getStatusCd() != 1) { // 0: 작성중, 1: 상신됨
            throw new RuntimeException("이 문서는 회수할 수 없는 상태입니다");
        }

        doc.setStatusCd(4); // 4: 회수됨
        doc.setStatusNm("회수됨");
        approvalDocRepository.save(doc);
    }





}
