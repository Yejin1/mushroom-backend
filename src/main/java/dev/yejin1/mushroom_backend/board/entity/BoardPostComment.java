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
import java.util.List;

@Entity
@Table(name = "board_post_comment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardPostComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 어떤 게시글에 속하는 댓글인지
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private BoardPost boardPost;

    // 대댓글 기능: 자기 부모 댓글
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private BoardPostComment parent;

    // 대댓글 트리 구조
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private List<BoardPostComment> children;

    @Builder.Default
    @Column(nullable = false)
    private int depth = 0;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private Long authorId;

    @Column(nullable = false, length = 50)
    private String authorName;

    @Builder.Default
    @Column(nullable = false)
    private int likeCount = 0;

    @Builder.Default
    @Column(nullable = false)
    private int dislikeCount = 0;

    @Column(nullable = false, length = 50)
    private String createdBy;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(length = 50)
    private String updatedBy;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Builder.Default
    @Column(nullable = false)
    private boolean isDeleted = false;
}
