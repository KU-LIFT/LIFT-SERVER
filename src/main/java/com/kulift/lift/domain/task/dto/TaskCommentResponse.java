package com.kulift.lift.domain.task.dto;

import java.time.LocalDateTime;

import com.kulift.lift.domain.auth.dto.UserResponse;
import com.kulift.lift.domain.task.entity.TaskComment;

import lombok.Builder;
import lombok.Getter;

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
