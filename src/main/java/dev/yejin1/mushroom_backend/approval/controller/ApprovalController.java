/**
 * ApprovalController
 * <p>
 * 전자결재 문서 컨트롤러
 *
 * <p>
 * 문서목록 조회
 * 문서작성
 * </p>
 *
 * @author Yejin1
 * @since 2025-05-17
 */
package dev.yejin1.mushroom_backend.approval.controller;

import dev.yejin1.mushroom_backend.approval.dto.ApprovalDocRequestDto;
import dev.yejin1.mushroom_backend.approval.dto.ApprovalDocResponseDto;
import dev.yejin1.mushroom_backend.approval.entity.ApprovalDoc;
import dev.yejin1.mushroom_backend.approval.entity.ApprovalDocBody;
import dev.yejin1.mushroom_backend.approval.entity.ApprovalForm;
import dev.yejin1.mushroom_backend.approval.service.ApprovalService;
import dev.yejin1.mushroom_backend.security.CustomUserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/approvals")
public class ApprovalController {

    private final ApprovalService approvalService;


    @GetMapping
    ResponseEntity<List<ApprovalDocResponseDto>> getAllDocs() {
        return ResponseEntity.ok(approvalService.getAllDocs());
    }

    @GetMapping("/list")
    public Page<ApprovalDocResponseDto> getDocList(@RequestParam Integer statusCd, @PageableDefault(size = 10, sort = "createDt", direction = Sort.Direction.DESC) Pageable pageable) {
        //사용자 ID 정보 세팅
        CustomUserPrincipal principal = (CustomUserPrincipal)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long usrId = principal.getUsrId();


        return approvalService.getDocList(usrId, statusCd, pageable);
    }

    @GetMapping("/formList")
    public List<ApprovalForm> getFormList() {
        return approvalService.getActiveFormList();
    }

    @GetMapping("/read")
    public Optional<ApprovalDocBody> getDocBody(@RequestParam Long docId) {
        return approvalService.getDocBody(docId);
    }


    @PostMapping
    public ResponseEntity<Long> createApproval(@RequestBody ApprovalDocRequestDto dto) {

        //로그인 정보
        CustomUserPrincipal principal = (CustomUserPrincipal)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        //작성자 세팅
        Long usrId = principal.getUsrId();
        dto.setWriter(usrId);

        Long id = approvalService.createApproval(dto);
        return ResponseEntity.ok(id);
    }

}
