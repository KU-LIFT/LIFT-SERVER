package com.kulift.lift.task.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import com.kulift.lift.task.entity.TaskComment;
import com.kulift.lift.user.dto.UserResponse;

@Getter
@Builder
public class TaskCommentResponse {
    private Long id;
    private String content;
    private UserResponse author;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static TaskCommentResponse from(TaskComment c) {
        return TaskCommentResponse.builder()
            .id(c.getId())
            .content(c.getContent())
            .author(UserResponse.from(c.getCreatedBy()))
            .createdAt(c.getCreatedAt())
            .updatedAt(c.getUpdatedAt())
            .build();
    }
}
