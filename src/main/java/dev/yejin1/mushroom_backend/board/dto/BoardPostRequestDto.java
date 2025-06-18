package dev.yejin1.mushroom_backend.board.dto;

import lombok.Data;
import lombok.Getter;


@Data
@Getter
public class BoardPostRequestDto {
    private Long boardMenuId;
    private String title;
    private Long authorId;
    private String content;
}
