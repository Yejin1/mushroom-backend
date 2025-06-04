package dev.yejin1.mushroom_backend.org.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "org_dept", schema = "mushroom")
@Data
public class OrgDept {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DEPT_ID")
    private Long deptId;

    @Column(name = "DEPT_NM", nullable = false, length = 100)
    private String deptNm;

    @Column(name = "PARENT_DEPT_ID")
    private Long parentDeptId;  // 트리 구조를 위한 부모 부서 ID

    @Column(name = "DEPT_DESC", length = 200)
    private String deptDesc;

    @Column(name = "DEPT_ORDER")
    private Integer deptOrder;

    @Column(name = "CREATED_DT", nullable = false)
    private LocalDateTime createdDt;

    @Column(name = "CREATED_ID", nullable = false, length = 45)
    private String createdId;

    @Column(name = "UPDATED_DT", nullable = false)
    private LocalDateTime updatedDt;

    @Column(name = "UPDATED_ID", nullable = false, length = 45)
    private String updatedId;
}
