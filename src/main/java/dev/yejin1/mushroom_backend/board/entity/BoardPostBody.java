/**
 * BoardPostBody
 *
 * 게시판 게시글 내용 Entity
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
@Table(name = "board_post_body")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardPostBody {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false, unique = true)
    private BoardPost boardPost;

    @Lob
    @Column(nullable = false)
    private String content;

    @Column(nullable = false, length = 50)
    private String createdBy;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(length = 50)
    private String updatedBy;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
