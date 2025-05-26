/**
 * OrgUsr
 *
 * 사용자 정보 테이블 Entity
 *
 * <p>
 * </p>
 *
 * @author Yejin1
 * @since 2025-05-17
 */
package dev.yejin1.mushroom_backend.org.entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "org_usr", schema = "mushroom")
@Data
@NoArgsConstructor
public class OrgUsr {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USR_ID")
    private Long usrId;

    @Column(name = "LOGIN_ID", nullable = false, unique = true, length = 45)
    private String loginId;

    @Column(name = "USR_NM", nullable = false, unique = true, length = 45)
    private String usrNm;

    @Column(name = "PWD", nullable = false, length = 200)
    private String pwd;

    @Column(name = "PWD_DATE", nullable = false, length = 8)
    private String pwdDate;

    @Column(name = "LOCK_YN", length = 1)
    private String lockYn = "N";

    @Column(name = "STATUS", length = 1)
    private String status;

    @Column(name = "EMP_NO", length = 20)
    private String empNo;

    @Column(name = "EMAIL", length = 100)
    private String email;

    @ManyToOne
    @JoinColumn(name = "POS_NO")
    private OrgPos pos;  // 직급 테이블과 연관관계 (ManyToOne)

    @Column(name = "EXTENSION_NO", length = 10)
    private String extensionNo;

    @Column(name = "JOB_DESC", length = 200)
    private String jobDesc;

    @Column(name = "PROFILE_BIO", length = 200)
    private String profileBio;

    @Lob
    @Column(name = "PROFILE_PHOTO")
    private byte[] profilePhoto;

    @Column(name = "JOIN_DATE", nullable = false, length = 8)
    private String joinDate;

    @Column(name = "CREATE_DT", nullable = false)
    private LocalDateTime createDt;

    @Column(name = "CREATE_ID", nullable = false, length = 45)
    private String createId;

    @Column(name = "UPDATE_DT", nullable = false)
    private LocalDateTime updateDt;

    @Column(name = "UPDATE_ID", nullable = false, length = 45)
    private String updateId;
}
