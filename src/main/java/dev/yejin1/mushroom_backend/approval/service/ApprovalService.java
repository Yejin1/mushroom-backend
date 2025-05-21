package dev.yejin1.mushroom_backend.approval.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import dev.yejin1.mushroom_backend.approval.dto.ApprovalDocRequestDto;
import dev.yejin1.mushroom_backend.approval.dto.ApprovalDocResponseDto;
import dev.yejin1.mushroom_backend.approval.entity.ApprovalDoc;
import dev.yejin1.mushroom_backend.approval.entity.ApprovalDocBody;
import dev.yejin1.mushroom_backend.approval.entity.ApprovalForm;
import dev.yejin1.mushroom_backend.approval.repository.ApprovalDocBodyRepository;
import dev.yejin1.mushroom_backend.approval.repository.ApprovalDocRepository;
import dev.yejin1.mushroom_backend.approval.repository.ApprovalFormRepository;
import dev.yejin1.mushroom_backend.org.entity.OrgUsr;
import dev.yejin1.mushroom_backend.org.repository.OrgUsrRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApprovalService {

    private final ApprovalDocRepository approvalDocRepository;
    private final ApprovalDocBodyRepository approvalDocBodyRepository;
    private final OrgUsrRepository orgUsrRepository;
    private final ApprovalFormRepository approvalFormRepository;

    public List<ApprovalDocResponseDto> getAllDocs() {
        return 
                approvalDocRepository.findAll().stream()
                .map(doc -> ApprovalDocResponseDto.builder()
                        .id(doc.getId())
                        .docNo(doc.getDocNo())
                        .formId(doc.getFormId())
                        .formNm(doc.getFormNm())
                        .title(doc.getTitle())
                        .writer(doc.getWriter())
                        .writerNm(doc.getWriterNm())
                        .statusCd(doc.getStatusCd())
                        .statusNm(doc.getStatusNm())
                        .currentUsr(doc.getCurrentUsr())
                        .createDt(doc.getCreateDt())
                        .completedDt(doc.getCompletedDt())
                        .urgentYn(doc.getUrgentYn())
                        .build())
                .collect(Collectors.toList());
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

    public Long createApproval(ApprovalDocRequestDto dto) {
        ApprovalDoc doc = new ApprovalDoc();
        doc.setFormId(dto.getFormId());
        doc.setTitle(dto.getTitle());
        doc.setWriter(dto.getWriter());
        doc.setUrgentYn(dto.getUrgentYn());
        doc.setStatusCd(0);
        doc.setStatusNm("작성중");
        doc.setCreateDt(LocalDateTime.now());

        OrgUsr writer = orgUsrRepository.findById(dto.getWriter())
                .orElseThrow(() -> new RuntimeException("작성자 없음"));
        ApprovalForm form = approvalFormRepository.findById(dto.getFormId())
                .orElseThrow(() -> new RuntimeException("양식 없음"));


        ApprovalDocBody body = new ApprovalDocBody();
        body.setFormContent(new ObjectMapper().valueToTree(dto.getFormContent()).toString());
        body.setLastEditedBy(dto.getWriter());
        body.setLastEditedDt(LocalDateTime.now());

        body.setDoc(doc);

        approvalDocRepository.save(doc);
        approvalDocBodyRepository.save(body);
        return doc.getId();
    }

}
