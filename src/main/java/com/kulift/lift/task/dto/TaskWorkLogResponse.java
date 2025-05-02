package com.kulift.lift.task.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import com.kulift.lift.task.entity.TaskWorkLog;
import com.kulift.lift.user.dto.UserResponse;

@Getter
@Builder
public class TaskWorkLogResponse {
    private Long id;
    private int durationMinutes;
    private String description;
    private UserResponse author;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static TaskWorkLogResponse from(TaskWorkLog w) {
        return TaskWorkLogResponse.builder()
            .id(w.getId())
            .durationMinutes(w.getDurationMinutes())
            .description(w.getDescription())
            .author(UserResponse.from(w.getLoggedBy()))
            .createdAt(w.getLoggedAt())
            .updatedAt(w.getUpdatedAt())
            .build();
    }
}
