package dev.yejin1.mushroom_backend.calendar.service;

import dev.yejin1.mushroom_backend.calendar.dto.ScheduleCreateRequestDto;
import dev.yejin1.mushroom_backend.calendar.dto.ScheduleDto;
import dev.yejin1.mushroom_backend.calendar.dto.ScheduleUpdateRequestDto;
import dev.yejin1.mushroom_backend.calendar.dto.TagDto;
import dev.yejin1.mushroom_backend.calendar.entity.Schedule;
import dev.yejin1.mushroom_backend.calendar.entity.ScheduleTag;
import dev.yejin1.mushroom_backend.calendar.repository.ScheduleRepository;
import dev.yejin1.mushroom_backend.calendar.repository.ScheduleTagRepository;
import dev.yejin1.mushroom_backend.org.entity.OrgUsr;
import dev.yejin1.mushroom_backend.org.repository.OrgUsrRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);

        List<Schedule> schedules = scheduleRepository
                .findByStartDateTimeLessThanEqualAndEndDateTimeGreaterThanEqual(
                        endDateTime, startDateTime);

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

        // 작성자 확인 (본인만 수정 가능)
        if (!schedule.getCreatedBy().getUsrId().equals(currentUserId)) {
            throw new AccessDeniedException("본인이 작성한 일정만 수정할 수 있습니다.");
        }

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

        return ScheduleDto.builder()
                .id(schedule.getId())
                .title(schedule.getTitle())
                .start(schedule.getStartDateTime())
                .end(schedule.getEndDateTime())
                .color(representativeColor)
                .tags(
                        schedule.getTags().stream()
                                .map(tag -> new TagDto(tag.getId(), tag.getName(), tag.getColor(), tag.getPriority()))
                                .toList()
                )
                .build();
    }

}
