package dev.yejin1.mushroom_backend.calendar.entity;
import dev.yejin1.mushroom_backend.org.entity.OrgUsr;
import jakarta.persistence.*;
import lombok.*;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id", nullable = false)
    private Schedule schedule;

    @Column(nullable = false, length = 50)
    private String tag;

    // 선택적으로 태그별 색상 저장 가능
    @Column(length = 20)
    private String color;
}
