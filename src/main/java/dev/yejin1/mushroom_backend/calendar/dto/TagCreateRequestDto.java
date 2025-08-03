package dev.yejin1.mushroom_backend.calendar.dto;

import dev.yejin1.mushroom_backend.calendar.entity.TagScopeType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TagCreateRequestDto {
    private String name;
    private String color;
    private Integer priority;
    private TagScopeType scopeType;
    private Long deptId;
}
