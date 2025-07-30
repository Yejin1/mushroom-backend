package dev.yejin1.mushroom_backend.calendar.entity;
import dev.yejin1.mushroom_backend.org.entity.OrgUsr;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "schedule_attendee", schema = "mushroom")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScheduleAttendee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id", nullable = false)
    private Schedule schedule;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usr_id", nullable = false)
    private OrgUsr attendee;

    // 참석 상태 (예: ATTENDING, DECLINED, TENTATIVE)
    @Column(nullable = false, length = 20)
    private String status;
}
