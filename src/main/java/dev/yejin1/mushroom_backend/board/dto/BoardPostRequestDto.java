package dev.yejin1.mushroom_backend.board.dto;

import dev.yejin1.mushroom_backend.approval.dto.ApprovalLineRequestDto;
import lombok.Data;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Data
@Getter
public class BoardPostRequestDto {
    private Long boardMenuId;
    private String title;
    private Long authorId;
    private String content;
}
