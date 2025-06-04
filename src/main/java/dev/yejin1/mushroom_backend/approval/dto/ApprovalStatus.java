package dev.yejin1.mushroom_backend.approval.dto;

public enum ApprovalStatus {
    CREATED,    // 상신
    WRITING,    // 순서대기
    WAITING,    // 결재대기
    APPROVED,   // 승인
    REJECTED,   // 반려
    RECALLED    // 회수
}
