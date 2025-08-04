package dev.yejin1.mushroom_backend.calendar.controller;

import dev.yejin1.mushroom_backend.calendar.dto.ScheduleCreateRequestDto;
import dev.yejin1.mushroom_backend.calendar.dto.ScheduleDto;
import dev.yejin1.mushroom_backend.calendar.dto.ScheduleUpdateRequestDto;
import dev.yejin1.mushroom_backend.calendar.service.ScheduleService;
import dev.yejin1.mushroom_backend.security.CustomUserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    @GetMapping
    public List<ScheduleDto> getSchedules(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate
    ) {

        return scheduleService.getSchedulesBetween(startDate, endDate);
    }

    @PostMapping
    public ResponseEntity<ScheduleDto> createSchedule(
            @RequestBody ScheduleCreateRequestDto request) {

        //로그인 정보
        CustomUserPrincipal principal = (CustomUserPrincipal)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //작성자 세팅
        Long currentUserId = principal.getUsrId();

        ScheduleDto createdSchedule = scheduleService.createSchedule(request, currentUserId);
        return ResponseEntity.ok(createdSchedule);
    }

    @PutMapping
    public ResponseEntity<ScheduleDto> updateSchedule(
            @RequestBody ScheduleUpdateRequestDto request) {

        //로그인 정보
        CustomUserPrincipal principal = (CustomUserPrincipal)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //작성자 세팅
        Long currentUserId = principal.getUsrId();

        Long id = request.getId();

        ScheduleDto updatedSchedule = scheduleService.updateSchedule(id, request, currentUserId);
        return ResponseEntity.ok(updatedSchedule);
    }

    //일정 삭제
    @DeleteMapping
    public ResponseEntity<Void> deletePost(@RequestParam Long id) {

        scheduleService.deleteSchedule(id);

        return ResponseEntity.noContent().build();
    }
}
