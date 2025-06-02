/**
 * BoardMenu
 *
 * 게시판 메뉴 정보 Entity
 *
 * <p>
 * </p>
 *
 * @author Yejin1
 * @since 2025-06-02
 */
package dev.yejin1.mushroom_backend.board.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "board_menu")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardMenu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private BoardMenu parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private List<BoardMenu> children;

    @Column(nullable = false)
    private int depth;

    @Column(length = 1, nullable = false)
    private String type; // 'f' = folder, 'b' = board

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "sort_order")
    private Integer sortOrder;

    @Column(nullable = false, length = 50)
    private String createdBy;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(length = 50)
    private String updatedBy;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
