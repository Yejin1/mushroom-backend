package dev.yejin1.mushroom_backend.approval.dto;

public enum ApprovalStatus {
    WRITING,    // 작성 중
    WAITING,    // 대기 중
    APPROVED,   // 승인 완료
    REJECTED    // 반려됨
}
