package dev.yejin1.mushroom_backend.org.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrgUserSimpleDto {
    private Long usrId;
    private String usrNm;
    private String empNo;
    private String posNm;     // 직급명
    private String deptNm;    // 부서명 추가
    private String email;
}
