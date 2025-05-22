package dev.yejin1.mushroom_backend.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Data
@Builder
public class LoginResponse {
    private String loginId;
    private String usrNm;
    private String token;
}
