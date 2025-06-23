package com.kulift.lift.domain.task.dto;

import java.time.LocalDateTime;

import com.kulift.lift.domain.auth.dto.UserResponse;
import com.kulift.lift.domain.task.entity.TaskWorkLog;

import lombok.Builder;
import lombok.Getter;

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
