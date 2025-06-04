package dev.yejin1.mushroom_backend.org.controller;

import dev.yejin1.mushroom_backend.org.dto.OrgUserSimpleDto;
import dev.yejin1.mushroom_backend.org.service.OrgUsrService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/usr")
@RequiredArgsConstructor
public class OrgUsrController {

    private final OrgUsrService orgUsrService;

    @GetMapping("/users")
    public ResponseEntity<List<OrgUserSimpleDto>> getAllUsers() {
        List<OrgUserSimpleDto> users = orgUsrService.getAllUsers();
        return ResponseEntity.ok(users);
    }
}
