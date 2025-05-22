package dev.yejin1.mushroom_backend.auth.controller;

import dev.yejin1.mushroom_backend.auth.AuthService;
import dev.yejin1.mushroom_backend.auth.dto.LoginRequest;
import dev.yejin1.mushroom_backend.auth.dto.LoginResponse;
import dev.yejin1.mushroom_backend.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}