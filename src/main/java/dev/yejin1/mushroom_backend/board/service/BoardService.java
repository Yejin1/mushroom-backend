/**
 * BoardService
 *
 * 게시판 서비스
 *
 * <p>
 *     게시판 메뉴 조회
 *     게시판 목록 조회
 *     게시글 작성
 * </p>
 *
 * @author Yejin1
 * @since 2025-06-03
 */
package dev.yejin1.mushroom_backend.board.service;

import dev.yejin1.mushroom_backend.board.dto.*;
import dev.yejin1.mushroom_backend.board.entity.BoardMenu;
import dev.yejin1.mushroom_backend.board.entity.BoardPost;
import dev.yejin1.mushroom_backend.board.entity.BoardPostBody;
import dev.yejin1.mushroom_backend.board.repository.BoardMenuRepository;
import dev.yejin1.mushroom_backend.board.repository.BoardPostBodyRepository;
import dev.yejin1.mushroom_backend.board.repository.BoardPostRepository;
import dev.yejin1.mushroom_backend.org.entity.OrgUsr;
import dev.yejin1.mushroom_backend.org.repository.OrgUsrRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardMenuRepository boardMenuRepository;
    private final BoardPostRepository boardPostRepository;
    private final BoardPostBodyRepository boardPostBodyRepository;
    private final OrgUsrRepository orgUsrRepository;

    public List<BoardMenuDto> buildBoardMenuTree(List<BoardMenu> menuList) {
        // 1. 엔티티 → DTO 변환
        List<BoardMenuDto> dtoList = menuList.stream()
                .map(menu -> BoardMenuDto.builder()
                        .id(menu.getBoardId())
                        .parentId(menu.getParent() != null ? menu.getParent().getBoardId() : null)
                        .name(menu.getName())
                        .type(menu.getType())
                        .depth(menu.getDepth())
                        .sortOrder(menu.getSortOrder())
                        .build())
                .collect(Collectors.toList());

        // 2. ID → DTO 맵 생성
        Map<Long, BoardMenuDto> dtoMap = dtoList.stream()
                .collect(Collectors.toMap(BoardMenuDto::getId, dto -> dto));

        // 3. 트리 구성
        List<BoardMenuDto> rootList = new ArrayList<>();
        for (BoardMenuDto dto : dtoList) {
            if (dto.getParentId() == null) {
                rootList.add(dto); // 루트
            } else {
                BoardMenuDto parent = dtoMap.get(dto.getParentId());
                if (parent != null) {
                    parent.getChildren().add(dto);
                }
            }
        }

        // 4. 정렬
        sortBoardMenuTree(rootList);

        return rootList;
    }

    private void sortBoardMenuTree(List<BoardMenuDto> list) {
        list.sort(Comparator.comparingInt(BoardMenuDto::getSortOrder));
        for (BoardMenuDto dto : list) {
            sortBoardMenuTree(dto.getChildren());
        }
    }

    public List<BoardMenuDto> getBoardMenuTree() {
        List<BoardMenu> allMenus = boardMenuRepository.findAllByOrderByDepthAscSortOrderAsc();
        return buildBoardMenuTree(allMenus);
    }

    public List<BoardWriteMenuDto> getBoardWriteMenu() {
        List<BoardWriteMenuDto> allMenus = boardMenuRepository.findBoardMenusByTypeOrderBySortOrder("b");
        return allMenus;
    }

    //게시글 목록 조회
    public Page<BoardPostListResponse> getPostList(Pageable pageable, long menuId) {
        Optional<BoardMenu> menu= boardMenuRepository.findById(menuId); //파라미터로 변경 필요
        Page<BoardPostListResponse> posts = boardPostRepository.findBoardPostsByBoardMenu(menu.get(), pageable);

        return posts;
    }

    //게시글 조회
    public BoardPostBodyResponse getPostBody(Long postId) {
        Optional<BoardPost> post = boardPostRepository.findById(postId);
        Optional<BoardPostBody> body = boardPostBodyRepository.findByBoardPost(post.get());
        BoardPostBodyResponse response = BoardPostBodyResponse.of(body.get());
        response.setTitle(post.get().getTitle());
        return response;
    }

    @Transactional
    public void updateViewCnt(Long postId) {
        Optional<BoardPost> post = boardPostRepository.findById(postId);
        post.get().setViewCount(post.get().getViewCount()+1);

        return;
    }


    @Transactional
    public Long writePost(BoardPostRequestDto dto) {

        // 1. 문서 저장
        BoardPost post = new BoardPost();
        BoardMenu menu = boardMenuRepository.findById(dto.getBoardMenuId()).orElseThrow();
        post.setBoardMenu(menu);
        post.setTitle(dto.getTitle());
        post.setAuthorId(dto.getAuthorId());
        post.setCreatedDt(LocalDateTime.now());

        OrgUsr writer = orgUsrRepository.findById(dto.getAuthorId())
                .orElseThrow(() -> new RuntimeException("작성자 없음"));

        post.setAuthorName(writer.getUsrNm());
        post.setCreatedBy(writer.getUsrNm());
        BoardPost savedPost = boardPostRepository.save(post);

        // 2. 본문 저장
        BoardPostBody body = new BoardPostBody();
        body.setContent(dto.getContent());

        body.setCreatedDt(LocalDateTime.now());
        body.setCreatedBy(writer.getUsrNm());
        body.setBoardPost(savedPost);
        boardPostBodyRepository.save(body);

        return savedPost.getId();
    }

    @Transactional
    public void deletePost(Long postId , Long currentUserId) throws AccessDeniedException {
        BoardPost post = boardPostRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글 없음"));

        // 작성자 확인
        if (!post.getAuthorId().equals(currentUserId)) {
            throw new RuntimeException("본인이 작성한 게시글만 삭제할 수 있습니다.");
        }

        boardPostRepository.delete(post);
    }



}
