package dev.yejin1.mushroom_backend.common.web;

import jakarta.persistence.OptimisticLockException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler({ OptimisticLockingFailureException.class, OptimisticLockException.class })
    public ResponseEntity<Map<String, Object>> handleOptimisticLocking(Exception ex) {
        log.warn("Optimistic lock conflict: {}", ex.getMessage());
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", Instant.now().toString());
        body.put("status", HttpStatus.CONFLICT.value());
        body.put("error", "Conflict");
        body.put("code", "OPTIMISTIC_LOCK_CONFLICT");
        body.put("message", "다른 사용자가 결재를 변경했습니다. 새로고침 후 다시 시도해주세요.");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }
}
