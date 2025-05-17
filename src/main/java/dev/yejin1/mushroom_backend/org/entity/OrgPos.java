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

    @Column(name = "CREATE_DT", nullable = false)
    private LocalDateTime createDt;

    @Column(name = "CREATE_ID", nullable = false, length = 45)
    private String createId;

    @Column(name = "UPDATE_DT", nullable = false)
    private LocalDateTime updateDt;

    @Column(name = "UPDATE_ID", nullable = false, length = 45)
    private String updateId;

    @OneToMany(mappedBy = "pos")
    private List<OrgUsr> users;
}
