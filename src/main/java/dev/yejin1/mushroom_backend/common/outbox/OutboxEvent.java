package dev.yejin1.mushroom_backend.common.outbox;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Getter
@Setter
@Table(name = "approval_event_outbox",
        indexes = {
                @Index(name = "idx_status_next_attempt", columnList = "status,nextAttemptAt"),
                @Index(name = "idx_created_at", columnList = "createdAt")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_unique_key", columnNames = {"uniqueKey"})
        }
)
public class OutboxEvent {

    public enum Status { PENDING, SENT }

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String eventType;           // "APPROVAL_COMPLETED"
    private Long aggregateId;           // approval_doc.id

    @Column(columnDefinition = "json")
    private String payloadJson;         // {"title": "...", "approver":"...", "link":"..."}

    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDING;

    private Integer attemptCnt = 0;
    private Instant nextAttemptAt;

    private String uniqueKey;           // 멱등키: "approve:{docId}"

    private Instant createdAt;
    private Instant updatedAt;

    public static OutboxEvent pending(String eventType, Long aggregateId, String payloadJson, String uniqueKey) {
        var e = new OutboxEvent();
        e.eventType = eventType;
        e.aggregateId = aggregateId;
        e.payloadJson = payloadJson;
        e.uniqueKey = uniqueKey;
        e.status = Status.PENDING;
        e.nextAttemptAt = Instant.now();
        return e;
    }

    @PrePersist void onCreate() {
        this.createdAt = this.updatedAt = Instant.now();
        if (this.nextAttemptAt == null) this.nextAttemptAt = Instant.now();
    }
    @PreUpdate void onUpdate() {
        this.updatedAt = Instant.now();
    }

    public void markSent() { this.status = Status.SENT; }

    public void backoff() {
        this.attemptCnt = (this.attemptCnt == null ? 0 : this.attemptCnt) + 1;
        long sec = (long) Math.min(60, Math.pow(2, this.attemptCnt)); // 1,2,4,8,...,60
        this.nextAttemptAt = Instant.now().plusSeconds(sec);
        this.status = Status.PENDING;
    }

}
