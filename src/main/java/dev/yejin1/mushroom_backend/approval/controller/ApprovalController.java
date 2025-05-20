package dev.yejin1.mushroom_backend.approval.controller;

import dev.yejin1.mushroom_backend.approval.dto.ApprovalDocRequestDto;
import dev.yejin1.mushroom_backend.approval.dto.ApprovalDocResponseDto;
import dev.yejin1.mushroom_backend.approval.entity.ApprovalDoc;
import dev.yejin1.mushroom_backend.approval.service.ApprovalService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
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

    @PostMapping
    public ResponseEntity<Long> createApproval(@RequestBody ApprovalDocRequestDto dto) {
        Long id = approvalService.createApproval(dto);
        return ResponseEntity.ok(id);
    }

}
