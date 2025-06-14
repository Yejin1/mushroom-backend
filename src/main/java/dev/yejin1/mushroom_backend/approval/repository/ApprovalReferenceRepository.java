package dev.yejin1.mushroom_backend.approval.repository;

import dev.yejin1.mushroom_backend.approval.entity.ApprovalReference;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApprovalReferenceRepository extends JpaRepository<ApprovalReference, Long> {
    List<ApprovalReference> findByApprovalDocId(Long docId);
}
