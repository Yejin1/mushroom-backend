package dev.yejin1.mushroom_backend.calendar.controller;

import dev.yejin1.mushroom_backend.calendar.dto.ScheduleCreateRequestDto;
import dev.yejin1.mushroom_backend.calendar.dto.ScheduleDto;
import dev.yejin1.mushroom_backend.calendar.dto.ScheduleUpdateRequestDto;
import dev.yejin1.mushroom_backend.calendar.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
            @RequestBody ScheduleCreateRequestDto request,
            Authentication authentication) {

        Long currentUserId = Long.valueOf(authentication.getName());

        ScheduleDto createdSchedule = scheduleService.createSchedule(request, currentUserId);
        return ResponseEntity.ok(createdSchedule);
    }

    @PutMapping
    public ResponseEntity<ScheduleDto> updateSchedule(
            @PathVariable Long id,
            @RequestBody ScheduleUpdateRequestDto request,
            Authentication authentication) {

        Long currentUserId = Long.valueOf(authentication.getName());

        ScheduleDto updatedSchedule = scheduleService.updateSchedule(id, request, currentUserId);
        return ResponseEntity.ok(updatedSchedule);
    }
}
