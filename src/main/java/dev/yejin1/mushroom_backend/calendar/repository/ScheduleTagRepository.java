package dev.yejin1.mushroom_backend.calendar.repository;

import dev.yejin1.mushroom_backend.calendar.entity.ScheduleTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleTagRepository extends JpaRepository<ScheduleTag, Long> {
}