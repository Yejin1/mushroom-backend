package dev.yejin1.mushroom_backend.org.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrgModalDto {
    private Long usrId;         // 아이디
    private String usrNm;       // 이름
    private String empNo;       // 사번
    private String posNm;       // 직급명
    private String deptNm;      // 부서명
    private String extensionNo; // 내선번호
    private String email;       // 이메일
    private String jobDesc;     // 담당업무
    private String profileBio;  // 한마디
}
