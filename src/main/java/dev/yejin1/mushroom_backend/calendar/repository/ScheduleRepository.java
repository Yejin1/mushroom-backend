package dev.yejin1.mushroom_backend.calendar.repository;

import dev.yejin1.mushroom_backend.calendar.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    // 일정이 기간과 겹치는 경우 모두 조회
    List<Schedule> findByStartDateTimeLessThanEqualAndEndDateTimeGreaterThanEqual(
            LocalDateTime endDate, LocalDateTime startDate
    );

    //사용자 ID와 일정 기준으로 모두 조회(태그 필터X)
    @Query("""
    SELECT DISTINCT s FROM Schedule s
    JOIN s.tags t
    LEFT JOIN s.attendees a
    WHERE s.startDateTime <= :endDate
      AND s.endDateTime >= :startDate
      AND (
        t.scopeType = 'COMPANY'
        OR (t.scopeType = 'DEPARTMENT' AND t.dept.id = :deptId)
        OR (t.scopeType = 'PERSONAL' AND t.usr.id = :userId)
        OR a.id = :userId
      )
""")
    List<Schedule> findAccessibleSchedules(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("userId") Long userId,
            @Param("deptId") Long deptId);

}