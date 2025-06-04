package dev.yejin1.mushroom_backend.approval.controller;


import dev.yejin1.mushroom_backend.approval.dto.ApprovalRejectRequestDto;
import dev.yejin1.mushroom_backend.approval.service.ApprovalLineService;
import dev.yejin1.mushroom_backend.security.CustomUserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/approval-line")
public class ApprovalLineController {

    private final ApprovalLineService approvalLineService;

    @PostMapping("/{lineId}/approve")
    public ResponseEntity<Void> approve(@PathVariable Long lineId) {
        CustomUserPrincipal principal = (CustomUserPrincipal)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long usrId = principal.getUsrId();

        approvalLineService.approve(lineId, usrId);
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
