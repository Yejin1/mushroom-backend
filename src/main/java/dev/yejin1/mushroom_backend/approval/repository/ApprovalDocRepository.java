package dev.yejin1.mushroom_backend.approval.repository;

import dev.yejin1.mushroom_backend.approval.entity.ApprovalDoc;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApprovalDocRepository  extends JpaRepository<ApprovalDoc, Long> {

    List<ApprovalDoc> findByStatusCd(Integer statusCd);

    List<ApprovalDoc> findByWriter(Long writer);

    List<ApprovalDoc> findByStatusCdAndUrgentYn(Integer statusCd, String urgentYn);

}
