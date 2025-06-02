/**
 * BoardPost
 *
 * 게시판 게시글 정보 Entity
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

@Entity
@Table(name = "board_post")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_menu_id", nullable = false)
    private BoardMenu boardMenu;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false)
    private Long authorId;

    @Column(nullable = false, length = 50)
    private String authorName;

    @Builder.Default
    @Column(nullable = false)
    private boolean isDeleted = false;

    @Builder.Default
    @Column(nullable = false)
    private int viewCount = 0;

    @Column(nullable = false, length = 50)
    private String createdBy;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(length = 50)
    private String updatedBy;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToOne(mappedBy = "boardPost", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private BoardPostBody body;
}
