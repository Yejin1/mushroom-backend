/**
 * OrgPos
 *
 * 사용자 직급 정보 테이블 Entity
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

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "org_pos", schema = "mushroom")
@Data
public class OrgPos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "POS_Id")
    private Long posId;

    @Column(name = "POS_NM", nullable = false, unique = true, length = 50)
    private String posNm;

    @Column(name = "POS_DESC", length = 200)
    private String posDesc;

    @Column(name = "CREATED_DT", nullable = false)
    private LocalDateTime createdDt;

    @Column(name = "CREATED_ID", nullable = false, length = 45)
    private String createdId;

    @Column(name = "UPDATED_DT", nullable = false)
    private LocalDateTime updatedDt;

    @Column(name = "UPDATED_ID", nullable = false, length = 45)
    private String updatedId;

    @OneToMany(mappedBy = "pos")
    private List<OrgUsr> users;
}
