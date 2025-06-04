package dev.yejin1.mushroom_backend.approval.dto;

import lombok.Data;

@Data
public class ApprovalRejectRequestDto {
    private Long docId;
    private String comment;
}