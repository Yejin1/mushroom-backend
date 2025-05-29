package dev.yejin1.mushroom_backend.org.dto;


import lombok.Data;

@Data
public class JoinRequest {
    private String usrNm;
    private String empNo;
    private String loginId;
    private String password;
}
