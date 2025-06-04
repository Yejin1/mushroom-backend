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

    @ManyToOne
    @JoinColumn(name = "DEPT_ID")
    private OrgDept dept;  // 사용자 주 부서

    @Column(name = "LOGIN_ID", unique = true, length = 45)
    private String loginId;

    @Column(name = "USR_NM", nullable = false, length = 45)
    private String usrNm;

    @Column(name = "PWD", length = 200)
    private String pwd;

    @Column(name = "PWD_DATE",  length = 8)
    private String pwdDate;

    @Column(name = "LOCK_YN", length = 1)
    private String lockYn = "N";

    @Column(name = "STATUS", length = 1)
    private String status;

    @Column(name = "EMP_NO", unique = true, length = 20)
    private String empNo;

    @Column(name = "BIRTH_DATE", length = 8)
    private String birthDate;

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

    @Column(name = "JOIN_DATE", length = 8)
    private String joinDate;

    @Column(name = "CREATED_DT", nullable = false)
    private LocalDateTime createdDt;

    @Column(name = "CREATED_ID", nullable = false, length = 45)
    private String createdId;

    @Column(name = "UPDATED_DT", nullable = false)
    private LocalDateTime updatedDt;

    @Column(name = "UPDATED_ID", nullable = false, length = 45)
    private String updatedId;
}
