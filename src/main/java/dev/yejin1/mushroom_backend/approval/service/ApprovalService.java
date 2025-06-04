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
import dev.yejin1.mushroom_backend.approval.dto.ApprovalLineRequestDto;
import dev.yejin1.mushroom_backend.approval.dto.ApprovalStatus;
import dev.yejin1.mushroom_backend.approval.entity.ApprovalDoc;
import dev.yejin1.mushroom_backend.approval.entity.ApprovalDocBody;
import dev.yejin1.mushroom_backend.approval.entity.ApprovalForm;
import dev.yejin1.mushroom_backend.approval.entity.ApprovalLine;
import dev.yejin1.mushroom_backend.approval.repository.ApprovalDocBodyRepository;
import dev.yejin1.mushroom_backend.approval.repository.ApprovalDocRepository;
import dev.yejin1.mushroom_backend.approval.repository.ApprovalFormRepository;
import dev.yejin1.mushroom_backend.approval.repository.ApprovalLineRepository;
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
    private final ApprovalLineRepository approvalLineRepository;

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

    @Transactional
    public Long createApproval(ApprovalDocRequestDto dto) {
        // 1. 문서 저장
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

        // 2. 본문 저장
        ApprovalDocBody body = new ApprovalDocBody();
        if ("Y".equals(dto.getEditorYn())) {
            body.setEditorContent(dto.getEditorContent());
            body.setEditorYn("Y");
        } else {
            body.setFormContent(new ObjectMapper().valueToTree(dto.getFormContent()).toString());
            body.setEditorYn("N");
        }
        body.setLastEditedBy(dto.getWriter());
        body.setLastEditedDt(LocalDateTime.now());
        body.setDoc(savedDoc);
        approvalDocBodyRepository.save(body);

        // 3. 결재선 저장
        List<ApprovalLineRequestDto> lineDtos = dto.getApprovalLine();
        if (lineDtos != null && !lineDtos.isEmpty()) {
            for (int i = 0; i < lineDtos.size(); i++) {
                ApprovalLineRequestDto lineDto = lineDtos.get(i);

                ApprovalLine line = ApprovalLine.builder()
                        .approvalDoc(savedDoc)
                        .approverId(lineDto.getUsrId())
                        .approverName(lineDto.getUsrNm())
                        .approverPosition(lineDto.getPosNm())
                        .approverDepartment(lineDto.getDeptNm())
                        .stepOrder(lineDto.getStepOrder())
                        .status(i == 0 ? ApprovalStatus.WAITING : ApprovalStatus.WRITING) // 첫 사람만 대기, 나머진 순서대기
                        .isFinalApprover(i == lineDtos.size() - 1)
                        .isParallel(false) // 병렬은 지금 없음
                        .build();

                approvalLineRepository.save(line);
            }
        }

        return savedDoc.getId();
    }

    //문서 내용 조회
    public Optional<ApprovalDocBody> getDocBody(Long docId) {
        return approvalDocBodyRepository.findById(docId);
    }

}
