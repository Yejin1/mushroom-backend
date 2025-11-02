package dev.yejin1.mushroom_backend.approval.service.handler;

import dev.yejin1.mushroom_backend.approval.service.handler.model.ApprovalContext;

public interface FinalApprovalHandler {
    void handle(ApprovalContext ctx);
}
