package dev.yejin1.mushroom_backend.org.dto;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class JoinResponse {
    private String usrNm;
    private String empNo;
    private String loginId;
}
