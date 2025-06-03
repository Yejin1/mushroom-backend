/**
 * BoardPostBodyRepository
 *
 * 게시판 -게시글 내용 테이블용 Repository
 *
 * <p>
 * </p>
 *
 * @author Yejin1
 * @since 2025-06-03
 */
package dev.yejin1.mushroom_backend.board.repository;


import dev.yejin1.mushroom_backend.board.dto.BoardPostBodyResponse;
import dev.yejin1.mushroom_backend.board.entity.BoardPost;
import dev.yejin1.mushroom_backend.board.entity.BoardPostBody;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BoardPostBodyRepository extends JpaRepository<BoardPostBody, Long> {
    Optional<BoardPostBody> findByBoardPost(BoardPost post);
}
