package dev.yejin1.mushroom_backend.approval.controller;


import dev.yejin1.mushroom_backend.approval.dto.ApprovalRejectRequestDto;
import dev.yejin1.mushroom_backend.approval.entity.ApprovalDoc;
import dev.yejin1.mushroom_backend.approval.entity.ApprovalForm;
import dev.yejin1.mushroom_backend.approval.entity.ApprovalLine;
import dev.yejin1.mushroom_backend.approval.repository.ApprovalFormRepository;
import dev.yejin1.mushroom_backend.approval.repository.ApprovalLineRepository;
import dev.yejin1.mushroom_backend.approval.service.ApprovalLineService;
import dev.yejin1.mushroom_backend.security.CustomUserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/approval-line")
public class ApprovalLineController {

    private final ApprovalLineService approvalLineService;
    // 문서 양식명 조회를 위한 Repository 주입
    private final ApprovalLineRepository approvalLineRepository;
    private final ApprovalFormRepository approvalFormRepository;

    @PostMapping("/{lineId}/approve")
    public ResponseEntity<Void> approve(@PathVariable Long lineId) {
        CustomUserPrincipal principal = (CustomUserPrincipal)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long usrId = principal.getUsrId();

        // lineId 기반으로 양식 code 조회
        ApprovalLine line = approvalLineRepository.findById(lineId)
                .orElseThrow(() -> new RuntimeException("결재선 정보 없음"));
        ApprovalDoc doc = line.getApprovalDoc();
        Long formId = Optional.ofNullable(doc.getFormId())
                .orElseThrow(() -> new RuntimeException("문서 양식 ID 없음"));
        ApprovalForm form = approvalFormRepository.findById(formId)
                .orElseThrow(() -> new RuntimeException("양식 정보 없음"));
        String formCode = form.getCode();

        approvalLineService.approve(lineId, usrId, formCode);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{lineId}/reject")
    public ResponseEntity<String> rejectApproval(@PathVariable Long lineId, @RequestBody ApprovalRejectRequestDto dto) {
        CustomUserPrincipal principal = (CustomUserPrincipal)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        approvalLineService.rejectApproval(lineId, principal.getUsrId(), dto.getComment());

        return ResponseEntity.ok("반려 처리 완료");
    }

    @PostMapping("/{docId}/withdraw")
    public ResponseEntity<String> withdrawApproval(@PathVariable Long docId) {
        CustomUserPrincipal principal = (CustomUserPrincipal)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        approvalLineService.withdrawApproval(docId, principal.getUsrId());

        return ResponseEntity.ok("회수 완료");
    }


}
