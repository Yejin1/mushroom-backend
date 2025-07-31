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

import dev.yejin1.mushroom_backend.board.dto.*;
import dev.yejin1.mushroom_backend.board.repository.BoardMenuRepository;
import dev.yejin1.mushroom_backend.board.repository.BoardPostRepository;
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

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/board")
public class BoardController {

    private final BoardService boardService;
    private final BoardPostRepository boardPostRepository;
    private final BoardMenuRepository boardMenuRepository;

    @GetMapping("/menuList") //메뉴 트리 조회
    public ResponseEntity<List<BoardMenuDto>> getMenuList() {

        List<BoardMenuDto> tree = boardService.getBoardMenuTree();
        return ResponseEntity.ok(tree);
    }

    @GetMapping("/writeMenuList") //글쓰기 게시판 선택용 목록
    public ResponseEntity<List<BoardWriteMenuDto>> writeMenuList() {

        List<BoardWriteMenuDto> tree = boardService.getBoardWriteMenu();
        return ResponseEntity.ok(tree);
    }

    @GetMapping("/list")
    public Page<BoardPostListResponse> getPostList(@PageableDefault(size = 10, sort = "createdDt", direction = Sort.Direction.DESC) Pageable pageable, long menuId) {
        Page<BoardPostListResponse> posts = boardService.getPostList(pageable, menuId);
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

    //게시글 작성
    @PostMapping("/write")
    public ResponseEntity<Long> writePost(@RequestBody BoardPostRequestDto dto) {
        //로그인 정보
        CustomUserPrincipal principal = (CustomUserPrincipal)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        //작성자 세팅
        Long usrId = principal.getUsrId();
        dto.setAuthorId(usrId);

        Long id = boardService.writePost(dto);
        return ResponseEntity.ok(id);
    }

    //게시글 삭제
    @DeleteMapping("/delete")
    public ResponseEntity<Void> deletePost(@RequestParam Long postId) throws AccessDeniedException {
        //로그인 정보
        CustomUserPrincipal principal = (CustomUserPrincipal)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        //사용자 정보 세팅
        Long usrId = principal.getUsrId();

        boardService.deletePost(postId, usrId);

        return ResponseEntity.noContent().build();
    }






}
