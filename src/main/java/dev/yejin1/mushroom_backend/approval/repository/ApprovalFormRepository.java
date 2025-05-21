package dev.yejin1.mushroom_backend.approval.repository;


import dev.yejin1.mushroom_backend.approval.entity.ApprovalForm;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApprovalFormRepository  extends JpaRepository<ApprovalForm, Long> {
}
