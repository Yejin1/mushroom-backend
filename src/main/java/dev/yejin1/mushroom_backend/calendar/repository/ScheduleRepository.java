package dev.yejin1.mushroom_backend.calendar.repository;

import dev.yejin1.mushroom_backend.calendar.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    // 일정이 기간과 겹치는 경우 모두 조회
    List<Schedule> findByStartDateTimeLessThanEqualAndEndDateTimeGreaterThanEqual(
            LocalDateTime endDate, LocalDateTime startDate
    );
}