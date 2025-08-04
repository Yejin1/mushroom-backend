package dev.yejin1.mushroom_backend.calendar.controller;

import dev.yejin1.mushroom_backend.calendar.dto.TagCreateRequestDto;
import dev.yejin1.mushroom_backend.calendar.dto.TagDto;
import dev.yejin1.mushroom_backend.calendar.service.TagService;
import dev.yejin1.mushroom_backend.org.repository.OrgUsrRepository;
import dev.yejin1.mushroom_backend.security.CustomUserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/schedules/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;
    private final OrgUsrRepository orgUsrRepository;


    // 태그 생성
    @PostMapping
    public ResponseEntity<TagDto> createTag(
            @RequestBody TagCreateRequestDto request) {


        //로그인 정보
        CustomUserPrincipal principal = (CustomUserPrincipal)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        //작성자 세팅
        Long currentUserId = principal.getUsrId();
        TagDto tag = tagService.createTag(request, currentUserId);
        return ResponseEntity.ok(tag);
    }

    // 사용자가 접근 가능한 태그 목록 조회
    @GetMapping
    public ResponseEntity<List<TagDto>> getAvailableTags() {
        //로그인 정보
        CustomUserPrincipal principal = (CustomUserPrincipal)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        //작성자 세팅
        Long currentUserId = principal.getUsrId();

        // 부서 정보
        Long currentDeptId = orgUsrRepository.findById(currentUserId).get().getDept().getDeptId();


        List<TagDto> tags = tagService.getAvailableTags(currentUserId, currentDeptId);
        return ResponseEntity.ok(tags);
    }

    //태그 삭제
    @DeleteMapping
    public ResponseEntity<Void> deleteTag(@RequestParam Long id) {

        tagService.deleteTag(id);

        return ResponseEntity.noContent().build();
    }
}
