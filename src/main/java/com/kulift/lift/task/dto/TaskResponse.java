package com.kulift.lift.task.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.kulift.lift.task.entity.Priority;
import com.kulift.lift.task.entity.Task;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TaskResponse {
	private Long id;
	private String name;
	private String description;
	private Long columnId;
	private Long assigneeId;
	private Priority priority;
	private LocalDateTime dueDate;
	private List<String> tags;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private Long createdById;
	private Long updatedById;

	public static TaskResponse from(Task task) {
		return TaskResponse.builder()
			.id(task.getId())
			.name(task.getName())
			.description(task.getDescription())
			.columnId(task.getColumn().getId())
			.assigneeId(task.getAssignee() != null ? task.getAssignee().getId() : null)
			.priority(task.getPriority())
			.dueDate(task.getDueDate())
			.tags(task.getTags())
			.createdAt(task.getCreatedAt())
			.updatedAt(task.getUpdatedAt())
			.createdById(task.getCreatedBy() != null ? task.getCreatedBy().getId() : null)
			.updatedById(task.getUpdatedBy() != null ? task.getUpdatedBy().getId() : null)
			.build();
	}
}
