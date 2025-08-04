package dev.yejin1.mushroom_backend.calendar.service;

import dev.yejin1.mushroom_backend.board.entity.BoardPost;
import dev.yejin1.mushroom_backend.calendar.dto.ScheduleCreateRequestDto;
import dev.yejin1.mushroom_backend.calendar.dto.ScheduleDto;
import dev.yejin1.mushroom_backend.calendar.dto.ScheduleUpdateRequestDto;
import dev.yejin1.mushroom_backend.calendar.dto.TagDto;
import dev.yejin1.mushroom_backend.calendar.entity.Schedule;
import dev.yejin1.mushroom_backend.calendar.entity.ScheduleTag;
import dev.yejin1.mushroom_backend.calendar.entity.TagScopeType;
import dev.yejin1.mushroom_backend.calendar.repository.ScheduleRepository;
import dev.yejin1.mushroom_backend.calendar.repository.ScheduleTagRepository;
import dev.yejin1.mushroom_backend.org.entity.OrgUsr;
import dev.yejin1.mushroom_backend.org.repository.OrgUsrRepository;
import dev.yejin1.mushroom_backend.security.CustomUserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final OrgUsrRepository orgUsrRepository;
    private final ScheduleTagRepository scheduleTagRepository;

    @Transactional(readOnly = true)
    public List<ScheduleDto> getSchedulesBetween(LocalDate startDate, LocalDate endDate) {

        //로그인 정보
        CustomUserPrincipal principal = (CustomUserPrincipal)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //작성자 세팅
        Long currentUserId = principal.getUsrId();
        // 부서 정보
        Long currentDeptId = orgUsrRepository.findById(currentUserId).get().getDept().getDeptId();

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);

        System.out.println("startDateTime = " + startDateTime);
        System.out.println("endDateTime = " + endDateTime);
        System.out.println("currentDeptId = " + currentDeptId);

        List<Schedule> schedules = scheduleRepository.findAccessibleSchedules(
                startDateTime, endDateTime, currentUserId, currentDeptId
        );

        return schedules.stream()
                .map(this::toDto)
                .toList();
    }


    @Transactional
    public ScheduleDto createSchedule(ScheduleCreateRequestDto dto, Long currentUserId) {
        OrgUsr creator = orgUsrRepository.findById(currentUserId)
                .orElseThrow(() -> new RuntimeException("작성자 없음"));

        // Schedule 생성
        Schedule schedule = Schedule.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .startDateTime(dto.getStartDateTime())
                .endDateTime(dto.getEndDateTime())
                .createdBy(creator)
                .build();

        // 태그 연결
        if (dto.getTagIds() != null && !dto.getTagIds().isEmpty()) {
            List<ScheduleTag> tags = scheduleTagRepository.findAllById(dto.getTagIds());
            schedule.setTags(tags);
        }

        // 참석자 연결
        if (dto.getAttendeeIds() != null && !dto.getAttendeeIds().isEmpty()) {
            List<OrgUsr> attendees = orgUsrRepository.findAllById(dto.getAttendeeIds());
            schedule.setAttendees(attendees);
        }

        Schedule saved = scheduleRepository.save(schedule);

        return toDto(saved);
    }

    @Transactional
    public ScheduleDto updateSchedule(Long id, ScheduleUpdateRequestDto dto, Long currentUserId) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("일정을 찾을 수 없습니다."));


        // 필드 업데이트
        if (dto.getTitle() != null) schedule.setTitle(dto.getTitle());
        if (dto.getDescription() != null) schedule.setDescription(dto.getDescription());
        if (dto.getStartDateTime() != null) schedule.setStartDateTime(dto.getStartDateTime());
        if (dto.getEndDateTime() != null) schedule.setEndDateTime(dto.getEndDateTime());

        // 태그 업데이트
        if (dto.getTagIds() != null) {
            List<ScheduleTag> tags = scheduleTagRepository.findAllById(dto.getTagIds());
            schedule.setTags(tags);
        }

        // 참석자 업데이트
        if (dto.getAttendeeIds() != null) {
            List<OrgUsr> attendees = orgUsrRepository.findAllById(dto.getAttendeeIds());
            schedule.setAttendees(attendees);
        }

        Schedule updated = scheduleRepository.save(schedule);

        return toDto(updated);
    }

    private ScheduleDto toDto(Schedule schedule) {

        String representativeColor = schedule.getTags().stream()
                .sorted(Comparator.comparingInt(ScheduleTag::getPriority))
                .map(ScheduleTag::getColor)
                .findFirst()
                .orElse("#3788d8"); // 기본 색상 (없을 경우)

        boolean allDay = schedule.getStartDateTime().toLocalTime().equals(LocalTime.MIDNIGHT)
                && schedule.getEndDateTime().toLocalTime().equals(LocalTime.of(23, 59, 59));

        return ScheduleDto.builder()
                .id(schedule.getId())
                .title(schedule.getTitle())
                .start(schedule.getStartDateTime())
                .end(schedule.getEndDateTime())
                .color(representativeColor)
                .allDay(allDay)
                .description(schedule.getDescription())
                .tags(
                        schedule.getTags().stream()
                                .map(tag -> new TagDto(
                                        tag.getId(),
                                        tag.getName(),
                                        tag.getColor(),
                                        tag.getPriority(),
                                        tag.getScopeType().toString(),
                                        tag.getUsr() != null ? tag.getUsr().getUsrId() : null,
                                        tag.getDept() != null ? tag.getDept().getDeptId() : null
                                ))
                                .toList()
                )
                .build();
    }

    public List<ScheduleTag> getAvailableTags(Long currentUserId, Long currentDeptId) {
        return scheduleTagRepository.findAll().stream()
                .filter(tag ->
                        tag.getScopeType() == TagScopeType.COMPANY ||
                                (tag.getScopeType() == TagScopeType.DEPARTMENT && tag.getDept().getDeptId().equals(currentDeptId)) ||
                                (tag.getScopeType() == TagScopeType.PERSONAL && tag.getUsr().getUsrId().equals(currentUserId))
                )
                .toList();
    }

    @Transactional
    public void deleteSchedule(Long id) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("일정을 찾을 수 없음"));

        schedule.getTags().clear();
        schedule.getAttendees().clear();
        scheduleRepository.delete(schedule);
    }


}
