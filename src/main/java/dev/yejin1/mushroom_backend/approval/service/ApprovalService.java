/**
 * ApprovalService
 *
 * 전자결재 문서 서비스
 *
 * <p>
 *     문서목록 조회
 *     문서작성
 * </p>
 *
 * @author Yejin1
 * @since 2025-05-17
 */
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
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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

    //문서 전체 목록 조회(미사용)
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
                        .createdDt(doc.getCreatedDt())
                        .completedDt(doc.getCompletedDt())
                        .urgentYn(doc.getUrgentYn())
                        .build())
                .collect(Collectors.toList());
    }

    //문서 목록 조회
    public Page<ApprovalDocResponseDto> getDocList(Long usrId, Integer StatusCd, Pageable pageable) {
        return approvalDocRepository.findApprovalDocsByConditions(usrId, StatusCd, pageable);
    }

    //결재 양식 목록
    public List<ApprovalForm> getActiveFormList() {
        return approvalFormRepository.findByActiveYn("Y");
    }

    //결재 양식 정보
    public Optional<ApprovalForm> getFormInfo(Long formId) {
        return approvalFormRepository.findById(formId);
    }

    //문서 작성
    @Transactional
    public Long createApproval(ApprovalDocRequestDto dto) {
        ApprovalDoc doc = new ApprovalDoc();
        doc.setFormId(dto.getFormId());
        doc.setTitle(dto.getTitle());
        doc.setWriter(dto.getWriter());
        doc.setUrgentYn(dto.getUrgentYn());
        doc.setStatusCd(0);
        doc.setStatusNm("작성중");
        doc.setCreatedDt(LocalDateTime.now());

        OrgUsr writer = orgUsrRepository.findById(dto.getWriter())
                .orElseThrow(() -> new RuntimeException("작성자 없음"));
        ApprovalForm form = approvalFormRepository.findById(dto.getFormId())
                .orElseThrow(() -> new RuntimeException("양식 없음"));

        doc.setWriterNm(writer.getUsrNm());
        doc.setFormNm(form.getName());
        ApprovalDoc savedDoc = approvalDocRepository.save(doc);

        ApprovalDocBody body = new ApprovalDocBody();

        if(dto.getEditorYn() != null){
            if(dto.getEditorYn().equals("Y")){
                body.setEditorContent(dto.getEditorContent());
                body.setEditorYn("Y");
            }
        }else{
            body.setFormContent(new ObjectMapper().valueToTree(dto.getFormContent()).toString());
            body.setEditorYn("N");
        }
        body.setLastEditedBy(dto.getWriter());
        body.setLastEditedDt(LocalDateTime.now());

        body.setDoc(savedDoc);

        approvalDocBodyRepository.save(body);
        return doc.getId();
    }

    public Optional<ApprovalDocBody> getDocBody(Long docId) {
        return approvalDocBodyRepository.findById(docId);
    }

}
