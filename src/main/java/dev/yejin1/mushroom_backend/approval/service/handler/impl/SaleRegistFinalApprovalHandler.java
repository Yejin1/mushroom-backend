package dev.yejin1.mushroom_backend.approval.service.handler.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.yejin1.mushroom_backend.approval.repository.ApprovalDocBodyRepository;
import dev.yejin1.mushroom_backend.approval.service.handler.FinalApprovalHandler;
import dev.yejin1.mushroom_backend.approval.service.handler.model.ApprovalContext;
import dev.yejin1.mushroom_backend.sale.service.SaleService;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

@Component("SALE_REGIST")
@RequiredArgsConstructor
public class SaleRegistFinalApprovalHandler implements FinalApprovalHandler {

    private final SaleService saleService;
    private final ApprovalDocBodyRepository approvalDocBodyRepository;
    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public void handle(ApprovalContext ctx) {
        // 판매 실적 등록 양식이면 redis 데이터 등록 (로컬 테스트용)
//        String formContents = approvalDocBodyRepository.getById(ctx.getDoc().getId()).getFormContent();
//        int amount;
//        try {
//            amount = MAPPER.readTree(formContents).path("amount").asInt();
//        } catch (JsonProcessingException e) {
//            throw new IllegalArgumentException("formContents JSON 파싱 실패", e);
//        }
//        saleService.handleSalesSubmitted(ctx.getDoc().getWriter(), amount);

        ctx.getDoc().setStatusCd(2);
        ctx.getDoc().setStatusNm("결재완료");
        ctx.getDoc().setCompletedDt(ctx.getNow());
    }
}
