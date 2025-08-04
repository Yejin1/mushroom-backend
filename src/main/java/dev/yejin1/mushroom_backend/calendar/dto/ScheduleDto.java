package dev.yejin1.mushroom_backend.calendar.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ScheduleDto {
    private Long id;
    private String title;
    private LocalDateTime start;
    private LocalDateTime end;
    private String color;
    private List<TagDto> tags;
    private String description;
    private Boolean allDay;
}
