package dev.yejin1.mushroom_backend.org.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "org_usr_dept_map", schema = "mushroom")
@Data
public class OrgUsrDeptMap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "USR_ID", nullable = false)
    private OrgUsr user;

    @ManyToOne
    @JoinColumn(name = "DEPT_ID", nullable = false)
    private OrgDept dept;

    @Column(name = "MAIN_YN", length = 1)
    private String mainYn = "Y"; // 겸직 여부 구분

    @Column(name = "CREATED_DT", nullable = false)
    private LocalDateTime createdDt;

    @Column(name = "CREATED_ID", nullable = false, length = 45)
    private String createdId;
}
