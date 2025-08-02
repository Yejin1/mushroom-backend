package dev.yejin1.mushroom_backend.calendar.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScheduleUpdateRequestDto {

    private Long id; // 수정할 일정 ID
    private String title;
    private String description;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

    private List<Long> tagIds;     // 선택된 태그
    private List<Long> attendeeIds; // 참석자
}