/**
 * ApprovalDocBodyRepository
 *
 * 전자결재 내용 테이블용 Repository
 *
 * <p>
 * </p>
 *
 * @author Yejin1
 * @since 2025-05-17
 */
package dev.yejin1.mushroom_backend.approval.repository;

import dev.yejin1.mushroom_backend.approval.entity.ApprovalDocBody;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ApprovalDocBodyRepository extends JpaRepository<ApprovalDocBody, Long> {
}
