package dev.yejin1.mushroom_backend.approval.service;


import dev.yejin1.mushroom_backend.approval.entity.ApprovalDoc;
import dev.yejin1.mushroom_backend.approval.repository.ApprovalDocRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ApprovalService {

    private final ApprovalDocRepository approvalDocRepository;


    public List<ApprovalDoc> getAllDocs() {
        return approvalDocRepository.findAll();
    }

    public Optional<ApprovalDoc> getDocById(Long id) {
        return approvalDocRepository.findById(id);
    }

    public ApprovalDoc saveDoc(ApprovalDoc doc) {
        return approvalDocRepository.save(doc);
    }

    public List<ApprovalDoc> getDocsByStatusCd(Integer statusCd) {
        return approvalDocRepository.findByStatusCd(statusCd);
    }

}
