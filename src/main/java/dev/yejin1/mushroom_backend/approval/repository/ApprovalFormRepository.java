/**
 * ApprovalFormRepository
 *
 * 전자결재 양식 테이블용 Repository
 *
 * <p>
 * </p>
 *
 * @author Yejin1
 * @since 2025-05-17
 */
package dev.yejin1.mushroom_backend.approval.repository;


import dev.yejin1.mushroom_backend.approval.entity.ApprovalForm;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApprovalFormRepository  extends JpaRepository<ApprovalForm, Long> {

    List<ApprovalForm> findByActiveYn(String ActiveYn);

}
