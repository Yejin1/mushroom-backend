package dev.yejin1.mushroom_backend.approval.controller;

import dev.yejin1.mushroom_backend.approval.dto.ApprovalDocRequestDto;
import dev.yejin1.mushroom_backend.approval.dto.ApprovalDocResponseDto;
import dev.yejin1.mushroom_backend.approval.entity.ApprovalDoc;
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
        CustomUserPrincipal principal = (CustomUserPrincipal)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Long usrId = principal.getUsrId();
        System.out.println("usrId = " + usrId);
        return approvalService.getDocList(usrId, statusCd, pageable);
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
