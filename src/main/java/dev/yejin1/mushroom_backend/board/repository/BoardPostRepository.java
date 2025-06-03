/**
 * BoardPostRepository
 *
 * 게시판 - 게시글 테이블용 Repository
 *
 * <p>
 * </p>
 *
 * @author Yejin1
 * @since 2025-06-03
 */
package dev.yejin1.mushroom_backend.board.repository;

import dev.yejin1.mushroom_backend.board.dto.BoardPostListResponse;
import dev.yejin1.mushroom_backend.board.entity.BoardMenu;
import dev.yejin1.mushroom_backend.board.entity.BoardPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardPostRepository extends JpaRepository<BoardPost, Long> {

    Page<BoardPostListResponse> findBoardPostsByBoardMenu(BoardMenu boardMenu, Pageable pageable);

}
