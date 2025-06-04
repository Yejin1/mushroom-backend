package dev.yejin1.mushroom_backend.board.dto;


import dev.yejin1.mushroom_backend.board.entity.BoardPost;
import dev.yejin1.mushroom_backend.board.entity.BoardPostBody;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class BoardPostBodyResponse {

    private Long id;
    private String title;

    private int viewCount;
    private String content;

    private LocalDateTime createdDt;
    private LocalDateTime updatedDt;

    public static BoardPostBodyResponse of(BoardPostBody body) {

        BoardPost post = body.getBoardPost();

        return BoardPostBodyResponse.builder()
                .content(body.getContent())
                .createdDt(body.getCreatedDt())
                .updatedDt(body.getUpdatedDt())
                .build();
    }
}