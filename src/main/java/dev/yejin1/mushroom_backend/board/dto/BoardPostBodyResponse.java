package dev.yejin1.mushroom_backend.board.dto;


import dev.yejin1.mushroom_backend.board.entity.BoardPostBody;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class BoardPostBodyResponse {

    private Long id;

    private int viewCount;
    private String content;

    private LocalDateTime createdDt;
    private LocalDateTime updatedDt;

    public static BoardPostBodyResponse of(BoardPostBody body) {
        return BoardPostBodyResponse.builder()
                .content(body.getContent())
                .createdDt(body.getCreatedDt())
                .updatedDt(body.getUpdatedDt())
                .build();
    }
}