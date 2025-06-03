/**
 * BoardMenuRepository
 *
 * 게시판 메뉴 테이블용 Repository
 *
 * <p>
 * </p>
 *
 * @author Yejin1
 * @since 2025-06-03
 */
package dev.yejin1.mushroom_backend.board.repository;

import dev.yejin1.mushroom_backend.approval.entity.ApprovalDocBody;
import dev.yejin1.mushroom_backend.board.entity.BoardMenu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardMenuRepository extends JpaRepository<BoardMenu, Long> {

    List<BoardMenu> findAllByOrderByDepthAscSortOrderAsc();

}
