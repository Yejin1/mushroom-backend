package dev.yejin1.mushroom_backend.board.dto;

import lombok.*;

@Getter
@Builder
public class BoardWriteMenuDto {
    private Long boardId;
    private String name;
    private String type; // 'f' or 'b'
    private int depth;
    private Integer sortOrder;
}
