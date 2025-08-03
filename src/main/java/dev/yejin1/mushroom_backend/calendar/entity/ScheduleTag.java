package dev.yejin1.mushroom_backend.calendar.entity;
import dev.yejin1.mushroom_backend.org.entity.OrgDept;
import dev.yejin1.mushroom_backend.org.entity.OrgUsr;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "schedule_tag", schema = "mushroom")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScheduleTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany(mappedBy = "tags")
    private List<Schedule> schedules;

    @Column(nullable = false, length = 50)
    private String name;

    // 선택적으로 태그별 색상 저장 가능
    @Column(length = 20)
    private String color;

    // 우선순위 (낮을수록 우선)
    @Column(nullable = false)
    private Integer priority;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TagScopeType scopeType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usr_id")
    private OrgUsr usr;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dept_id")
    private OrgDept dept;

}
