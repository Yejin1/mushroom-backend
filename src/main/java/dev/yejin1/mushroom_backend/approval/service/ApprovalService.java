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
import dev.yejin1.mushroom_backend.approval.dto.*;
import dev.yejin1.mushroom_backend.approval.entity.*;
import dev.yejin1.mushroom_backend.approval.repository.*;
import dev.yejin1.mushroom_backend.org.entity.OrgDept;
import dev.yejin1.mushroom_backend.org.entity.OrgUsr;
import dev.yejin1.mushroom_backend.org.repository.OrgDeptRepository;
import dev.yejin1.mushroom_backend.org.repository.OrgUsrRepository;
import dev.yejin1.mushroom_backend.sale.service.SaleService;
import dev.yejin1.mushroom_backend.security.CustomUserPrincipal;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private final ApprovalReferenceRepository approvalReferenceRepository;
    private final OrgDeptRepository orgDeptRepository;
    private final SaleService saleService;

    //문서 목록 조회
    public Page<ApprovalDocResponseDto> getDocList(
            Long usrId,
            String boxType,
            String searchType,
            String keyword,
            String startDate,
            String endDate,
            Pageable pageable) {

        return switch (boxType) {
            case "my-approval" -> approvalDocRepository.findMyApprovalBox(usrId, searchType, keyword, startDate, endDate, pageable);
            case "my-in-progress" -> approvalDocRepository.findMyProcessingBox(usrId, searchType, keyword, startDate, endDate, pageable);
            case "my-completed" -> approvalDocRepository.findMyCompletedBox(usrId, searchType, keyword, startDate, endDate, pageable);
            case "my-rejected" -> approvalDocRepository.findMyRejectedBox(usrId, searchType, keyword, startDate, endDate, pageable);
            case "my-referenced" -> approvalDocRepository.findMyReferenceBox(usrId, searchType, keyword, startDate, endDate, pageable);
            case "dept-completed" -> approvalDocRepository.findDeptCompletedBox(usrId, searchType, keyword, startDate, endDate, pageable);
            case "dept-referenced" -> approvalDocRepository.findDeptReferenceBox(usrId, searchType, keyword, startDate, endDate, pageable);
            default -> Page.empty();
        };
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
        doc.setIdempotencyKey(dto.getIdempotencyKey());
        doc.setStatusCd(1);
        doc.setStatusNm("상신됨");
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
        List<ApprovalLine> approvalLines = new ArrayList<>();

        // 3-1. 작성자 결재선 먼저 추가 (stepOrder 0)
        ApprovalLine writerLine = ApprovalLine.builder()
                .approvalDoc(savedDoc)
                .approverId(writer.getUsrId())
                .approverName(writer.getUsrNm())
                .approverPosition(writer.getPos() != null ? writer.getPos().getPosNm() : null)
                .approverDepartment(writer.getDept() != null ? writer.getDept().getDeptNm() : null)
                .stepOrder(0)
                .status(ApprovalStatus.CREATED)
                .isFinalApprover(false)
                .isParallel(false)
                .build();

        approvalLines.add(writerLine);

        // 3-2. 프론트에서 넘겨준 결재자 결재선 추가 (stepOrder는 1부터 시작)
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
                        .stepOrder(i + 1) // 작성자가 0번이므로 +1부터
                        .status(i == 0 ? ApprovalStatus.WAITING : ApprovalStatus.WRITING)
                        .isFinalApprover(i == lineDtos.size() - 1)
                        .isParallel(false)
                        .build();

                approvalLines.add(line);
            }
        }

        approvalLineRepository.saveAll(approvalLines);

        //판매 실적 등록 양식이면 redis 데이터 등록
        /* kafka 이슈로 주석 처리
        if (doc.getFormId() == 6L) {
            saleService.handleSaleDoc(dto);
        }
        */
        
        
        return savedDoc.getId();
    }


    //문서 내용 조회
    public ApprovalDocDetailResponseDto getDocBodyWithLines(Long docId) {
        ApprovalDocBody body = approvalDocBodyRepository.findById(docId)
                .orElseThrow(() -> new RuntimeException("본문 없음"));

        List<ApprovalLine> lines = approvalLineRepository.findByApprovalDocId(body.getDoc().getId());

        List<ApprovalLineDto> lineDtos = lines.stream().map(l -> {
            OrgUsr approver = orgUsrRepository.findById(l.getApproverId())
                    .orElseThrow(() -> new RuntimeException("결재자 정보 없음"));

            return ApprovalLineDto.builder()
                    .approverId(l.getApproverId())
                    .empNo(approver.getEmpNo())
                    .approverName(l.getApproverName())
                    .approverPosition(l.getApproverPosition())
                    .approverDepartment(l.getApproverDepartment())
                    .stepOrder(l.getStepOrder())
                    .status(l.getStatus())
                    .isFinalApprover(l.isFinalApprover())
                    .comment(l.getComment())
                    .approvedDt(l.getApprovedDt())
                    .build();
        }).toList();

        return ApprovalDocDetailResponseDto.builder()
                .body(body)
                .approvalLines(lineDtos)
                .build();
    }


    //참조
    @Transactional
    public void addReference(ApprovalReferenceRequestDto dto) {
        ApprovalDoc doc = approvalDocRepository.findById(dto.getDocId())
                .orElseThrow(() -> new RuntimeException("문서 없음"));

        //로그인 정보
        CustomUserPrincipal principal = (CustomUserPrincipal)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        //작성자
        OrgUsr writer = orgUsrRepository.findById(principal.getUsrId())
                .orElseThrow(() -> new RuntimeException("작성자 없음"));

        // 개인 참조자 등록
        if (dto.getRefUsrIds() != null) {
            for (Long usrId : dto.getRefUsrIds()) {
                OrgUsr usr = orgUsrRepository.findById(usrId)
                        .orElseThrow(() -> new RuntimeException("사용자 없음"));

                ApprovalReference ref = ApprovalReference.builder()
                        .approvalDoc(doc)
                        .refUsr(usr)
                        .refUsrName(usr.getUsrNm())
                        .refType("USER")
                        .createdBy(writer.getUsrNm())
                        .build();

                approvalReferenceRepository.save(ref);
            }
        }

        // 부서 참조자 등록
        if (dto.getRefDeptIds() != null) {
            for (Long deptId : dto.getRefDeptIds()) {
                OrgDept dept = orgDeptRepository.findById(deptId)
                        .orElseThrow(() -> new RuntimeException("부서 없음"));

                ApprovalReference ref = ApprovalReference.builder()
                        .approvalDoc(doc)
                        .refDept(dept)
                        .refDeptName(dept.getDeptNm())
                        .refType("DEPT")
                        .createdBy(writer.getUsrNm())
                        .build();

                approvalReferenceRepository.save(ref);
            }
        }
    }



    public ApprovalReferenceResponseDto getApprovalReference(Long docId) {
        List<ApprovalReference> refs = approvalReferenceRepository.findByApprovalDocId(docId);

        List<UserRefDto> userRefs = new ArrayList<>();
        List<DeptRefDto> deptRefs = new ArrayList<>();

        for (ApprovalReference ref : refs) {
            if ("USER".equals(ref.getRefType()) && ref.getRefUsr() != null) {
                userRefs.add(new UserRefDto(
                        ref.getId(),
                        ref.getRefUsr().getUsrId(),
                        ref.getRefUsrName(),
                        ref.getCreatedBy(),
                        ref.getCreatedDt()
                ));
            } else if ("DEPT".equals(ref.getRefType()) && ref.getRefDept() != null) {
                deptRefs.add(new DeptRefDto(
                        ref.getId(),
                        ref.getRefDept().getDeptId(),
                        ref.getRefDeptName(),
                        ref.getCreatedBy(),
                        ref.getCreatedDt()
                ));
            }
        }

        return new ApprovalReferenceResponseDto(userRefs, deptRefs);
    }



}
