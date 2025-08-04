package dev.yejin1.mushroom_backend.calendar.repository;

import dev.yejin1.mushroom_backend.calendar.entity.ScheduleTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ScheduleTagRepository extends JpaRepository<ScheduleTag, Long> {
    @Query("SELECT COALESCE(MAX(t.priority), 0) FROM ScheduleTag t")
    Optional<Integer> findMaxPriority();
}