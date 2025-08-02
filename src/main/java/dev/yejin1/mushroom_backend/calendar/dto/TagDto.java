package dev.yejin1.mushroom_backend.calendar.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TagDto {
    private Long id;
    private String name;
    private String color;
    private Integer order;
}