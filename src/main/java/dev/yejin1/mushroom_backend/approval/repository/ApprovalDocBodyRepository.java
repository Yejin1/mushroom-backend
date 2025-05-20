package dev.yejin1.mushroom_backend.approval.repository;

import dev.yejin1.mushroom_backend.approval.entity.ApprovalDocBody;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApprovalDocBodyRepository extends JpaRepository<ApprovalDocBody, Long> {
}
