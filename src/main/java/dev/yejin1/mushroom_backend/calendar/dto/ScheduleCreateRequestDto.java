package dev.yejin1.mushroom_backend.calendar.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScheduleCreateRequestDto {

    private String title;
    private String description;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

    private List<Long> tagIds;     // 태그 ID 리스트
    private List<Long> attendeeIds; // 참석자 ID 리스트
}
