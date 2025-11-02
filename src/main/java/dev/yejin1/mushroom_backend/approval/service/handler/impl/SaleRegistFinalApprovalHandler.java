package dev.yejin1.mushroom_backend.approval.service.handler.impl;

import dev.yejin1.mushroom_backend.approval.service.handler.FinalApprovalHandler;
import dev.yejin1.mushroom_backend.approval.service.handler.model.ApprovalContext;
import org.springframework.stereotype.Component;

@Component("SALE_REGIST")
public class SaleRegistFinalApprovalHandler implements FinalApprovalHandler {

    @Override
    public void handle(ApprovalContext ctx) {
        // todo : 결재 완료 시 매출 등록 처리
        ctx.getDoc().setStatusCd(2);
        ctx.getDoc().setStatusNm("결재완료");
        ctx.getDoc().setCompletedDt(ctx.getNow());
    }
}
