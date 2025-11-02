package dev.yejin1.mushroom_backend.approval.service.handler.impl;

import dev.yejin1.mushroom_backend.approval.service.handler.FinalApprovalHandler;
import dev.yejin1.mushroom_backend.approval.service.handler.model.ApprovalContext;
import org.springframework.stereotype.Component;

@Component("VACATION")
public class VacationFinalApprovalHandler implements FinalApprovalHandler {

    @Override
    public void handle(ApprovalContext ctx) {
        // to do: 휴가 승인 시 캘린더에 추가
        ctx.getDoc().setStatusCd(2);
        ctx.getDoc().setStatusNm("결재완료");
        ctx.getDoc().setCompletedDt(ctx.getNow());
    }
}
