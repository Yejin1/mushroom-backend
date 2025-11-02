package dev.yejin1.mushroom_backend.approval.service.handler.impl;

import dev.yejin1.mushroom_backend.approval.service.handler.FinalApprovalHandler;
import dev.yejin1.mushroom_backend.approval.service.handler.model.ApprovalContext;
import org.springframework.stereotype.Component;

@Component("DEFAULT")
public class DefaultFinalApprovalHandler implements FinalApprovalHandler {

    @Override
    public void handle(ApprovalContext ctx) {
        // 기존 기본 로직: 문서 상태 완료 처리
        ctx.getDoc().setStatusCd(2);
        ctx.getDoc().setStatusNm("결재완료");
        ctx.getDoc().setCompletedDt(ctx.getNow());
    }
}
