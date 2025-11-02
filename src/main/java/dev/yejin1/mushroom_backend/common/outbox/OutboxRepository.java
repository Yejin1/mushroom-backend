package dev.yejin1.mushroom_backend.common.outbox;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface OutboxRepository extends JpaRepository<OutboxEvent, Long> {

    Optional<OutboxEvent> findByUniqueKey(String uniqueKey);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        select o from OutboxEvent o
        where o.status =  dev.yejin1.mushroom_backend.common.outbox.OutboxEvent.Status.PENDING
          and o.nextAttemptAt <= :now
        order by o.createdAt asc
        """)
    List<OutboxEvent> pickPendingForDispatch(@Param("now") Instant now, Pageable pageable);
}
