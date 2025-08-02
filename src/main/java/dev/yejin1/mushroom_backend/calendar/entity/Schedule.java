package dev.yejin1.mushroom_backend.calendar.entity;

import dev.yejin1.mushroom_backend.org.entity.OrgUsr;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "schedule", schema = "mushroom")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 일정 제목
    @Column(nullable = false, length = 200)
    private String title;

    // 일정 설명
    @Column(columnDefinition = "TEXT")
    private String description;

    // 시작일시
    @Column(nullable = false)
    private LocalDateTime startDateTime;

    // 종료일시
    @Column(nullable = false)
    private LocalDateTime endDateTime;

    // 생성자 (작성자)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private OrgUsr createdBy;

    // 참석자 리스트 (N:M 관계)
    @ManyToMany
    @JoinTable(
            name = "schedule_attendee",
            joinColumns = @JoinColumn(name = "schedule_id"),
            inverseJoinColumns = @JoinColumn(name = "usr_id")
    )
    private List<OrgUsr> attendees;

    //태그 연관관계
    @ManyToMany
    @JoinTable(
            name = "schedule_tag_map",
            schema = "mushroom",
            joinColumns = @JoinColumn(name = "schedule_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<ScheduleTag> tags;

    // 생성일
    @Column(nullable = false)
    private LocalDateTime createdDt;

    // 수정일
    @Column(nullable = false)
    private LocalDateTime updatedDt;

    @PrePersist
    protected void onCreate() {
        this.createdDt = LocalDateTime.now();
        this.updatedDt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedDt = LocalDateTime.now();
    }

}
