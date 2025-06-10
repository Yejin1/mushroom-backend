/**
 * BoardController
 * <p>
 * 게시판 컨트롤러
 *
 * <p>
 * 게시글 목록 조회
 * 게시글 작성/조회
 * </p>
 *
 * @author Yejin1
 * @since 2025-06-02
 */
package dev.yejin1.mushroom_backend.board.controller;

import dev.yejin1.mushroom_backend.approval.dto.ApprovalDocResponseDto;
import dev.yejin1.mushroom_backend.approval.entity.ApprovalDocBody;
import dev.yejin1.mushroom_backend.board.dto.BoardMenuDto;
import dev.yejin1.mushroom_backend.board.dto.BoardPostBodyResponse;
import dev.yejin1.mushroom_backend.board.dto.BoardPostListResponse;
import dev.yejin1.mushroom_backend.board.entity.BoardMenu;
import dev.yejin1.mushroom_backend.board.entity.BoardPost;
import dev.yejin1.mushroom_backend.board.entity.BoardPostBody;
import dev.yejin1.mushroom_backend.board.repository.BoardMenuRepository;
import dev.yejin1.mushroom_backend.board.service.BoardService;
import dev.yejin1.mushroom_backend.security.CustomUserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/board")
public class BoardController {

    private final BoardService boardService;
    private final BoardMenuRepository boardMenuRepository;

    @GetMapping("/menuList") //메뉴 트리 조회
    public ResponseEntity<List<BoardMenuDto>> getMenuList() {

        List<BoardMenuDto> tree = boardService.getBoardMenuTree();
        return ResponseEntity.ok(tree);
    }

    @GetMapping("/list")
    public Page<BoardPostListResponse> getPostList(@PageableDefault(size = 10, sort = "createdDt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<BoardPostListResponse> posts = boardService.getPostList(pageable);
        return posts;
    }

    //게시글 내용 조회
    @GetMapping("/read")
    public BoardPostBodyResponse getPostBody(@RequestParam Long postId) {
        return boardService.getPostBody(postId);
    }

    //게시글 조회 수 증가
    @GetMapping("/viewCnt")
    public ResponseEntity<Void> updateViewCnt(@RequestParam Long postId) {
        boardService.updateViewCnt(postId);
        return ResponseEntity.ok().build();

    }




}
