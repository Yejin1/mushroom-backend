package dev.yejin1.mushroom_backend.approval.service.handler.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.yejin1.mushroom_backend.approval.repository.ApprovalDocBodyRepository;
import dev.yejin1.mushroom_backend.approval.service.handler.FinalApprovalHandler;
import dev.yejin1.mushroom_backend.approval.service.handler.model.ApprovalContext;
import dev.yejin1.mushroom_backend.calendar.dto.ScheduleCreateRequestDto;
import dev.yejin1.mushroom_backend.calendar.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;

@Component("VACATION")
@RequiredArgsConstructor
public class VacationFinalApprovalHandler implements FinalApprovalHandler {

    private final ApprovalDocBodyRepository approvalDocBodyRepository;
    private final ScheduleService scheduleService;
    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public void handle(ApprovalContext ctx) {
        // to do: 휴가 승인 시 캘린더에 추가
        String formContents = approvalDocBodyRepository.findById(ctx.getDoc().getId())
                .orElseThrow(() -> new RuntimeException("본문 없음"))
                .getFormContent();
        String startDateStr, endDateStr, writerNm;
        try {
            startDateStr = MAPPER.readTree(formContents).path("startDate").asText();
            endDateStr = MAPPER.readTree(formContents).path("endDate").asText();
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("formContents JSON 파싱 실패", e);
        }
        writerNm = ctx.getDoc().getWriterNm();

        // 스케줄 생성: (이름) 휴가, 종일 이벤트로 생성
        LocalDate startDate = LocalDate.parse(startDateStr);
        LocalDate endDate = LocalDate.parse(endDateStr);
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.of(23, 59, 59));

        ScheduleCreateRequestDto req = ScheduleCreateRequestDto.builder()
                .title(writerNm + " 휴가")
                .description("휴가 승인 자동 등록")
                .startDateTime(startDateTime)
                .endDateTime(endDateTime)
                .tagIds(new ArrayList<>(Arrays.asList(1L)))
                .build();

        // 작성자 기준으로 일정 생성
        scheduleService.createSchedule(req, ctx.getDoc().getWriter());

        ctx.getDoc().setStatusCd(2);
        ctx.getDoc().setStatusNm("결재완료");
        ctx.getDoc().setCompletedDt(ctx.getNow());
    }
}
