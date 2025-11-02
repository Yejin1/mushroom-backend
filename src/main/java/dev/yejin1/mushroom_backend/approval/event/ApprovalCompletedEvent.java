package dev.yejin1.mushroom_backend.approval.event;

// 도메인 이벤트 DTO (트랜잭션 커밋 이후에만 처리)
public record ApprovalCompletedEvent(
        Long docId,
        String title,
        String approverName,
        String link,          // 예: "/approval/docs/{id}"
        String uniqueKey      // 예: "approve:{docId}" (문서당 1회 전송)
) {}