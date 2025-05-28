package dev.yejin1.mushroom_backend.org.dto;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SubscriptCheckResponse {
    private Integer result; // 0:유저 없음, 1:가입 가능, 2:가입됨
    private String message;
}
