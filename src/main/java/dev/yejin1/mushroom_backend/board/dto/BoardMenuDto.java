package dev.yejin1.mushroom_backend.board.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardMenuDto {
    private Long id;
    private Long parentId;
    private String name;
    private String type; // 'f' or 'b'
    private int depth;
    private int sortOrder;

    @Builder.Default
    private List<BoardMenuDto> children = new ArrayList<>();
}
