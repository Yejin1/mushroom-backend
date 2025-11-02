package dev.yejin1.mushroom_backend.approval.service.handler.model;

import dev.yejin1.mushroom_backend.approval.entity.ApprovalDoc;
import dev.yejin1.mushroom_backend.approval.entity.ApprovalLine;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ApprovalContext {
    private final String formCode;
    private final ApprovalDoc doc;
    private final ApprovalLine line;
    private final LocalDateTime now;
}
