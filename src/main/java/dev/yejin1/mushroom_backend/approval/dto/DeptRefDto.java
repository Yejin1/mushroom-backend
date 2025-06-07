package dev.yejin1.mushroom_backend.approval.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DeptRefDto {
    private Long id;
    private Long deptId;
    private String deptName;
    private String createdBy;
    private LocalDateTime createdDt;
}