package dev.yejin1.mushroom_backend.org.controller;

import dev.yejin1.mushroom_backend.auth.dto.LoginRequest;
import dev.yejin1.mushroom_backend.org.dto.JoinRequest;
import dev.yejin1.mushroom_backend.org.dto.JoinResponse;
import dev.yejin1.mushroom_backend.org.dto.SubscriptCheckRequest;
import dev.yejin1.mushroom_backend.org.dto.SubscriptCheckResponse;
import dev.yejin1.mushroom_backend.org.service.JoinService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/join")
@RequiredArgsConstructor
public class JoinController {

    private final JoinService joinService;

    @PostMapping("/checkSubscript")
    public ResponseEntity<SubscriptCheckResponse> checkSubscript(@RequestBody SubscriptCheckRequest request) {
        return ResponseEntity.ok(joinService.checkSubscript(request));
    }

    @GetMapping("/checkId")
    public ResponseEntity<Boolean> checkId(@RequestParam String loginId){
        return ResponseEntity.ok(joinService.checkId(loginId));
    }

    @PostMapping("/submit")
    public ResponseEntity<JoinResponse> join(@RequestBody JoinRequest request) {
        System.out.println("called Join");
        return ResponseEntity.ok(joinService.join(request));
    }
}
