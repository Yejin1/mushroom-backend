package dev.yejin1.mushroom_backend.board.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Builder
public class BoardPostListResponse {

    private Long id;
    private String title;
    private String authorName;
    private LocalDateTime createdDt;
    private int viewCount;
}
