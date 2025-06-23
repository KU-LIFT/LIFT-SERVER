package com.kulift.lift.domain.task.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.kulift.lift.domain.task.entity.Priority;
import com.kulift.lift.domain.task.entity.Task;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TaskResponse {
	private Long id;
	private String name;
	private String description;
	private String projectKey;
	private Long columnId;
	private String assignee;
	private Priority priority;
	private LocalDateTime dueDate;
	private List<String> tags;
	private String githubBranch;
	private String githubPullRequestUrl;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private String createdBy;
	private String updatedBy;

	public static TaskResponse from(Task task) {
		return TaskResponse.builder()
			.id(task.getId())
			.name(task.getName())
			.description(task.getDescription())
			.projectKey(task.getColumn().getBoard().getProjectKey())
			.columnId(task.getColumn().getId())
			.assignee(task.getAssignee() != null ? task.getAssignee().getName() : null)
			.priority(task.getPriority())
			.dueDate(task.getDueDate())
			.tags(task.getTags())
			.githubBranch(task.getGithubBranch())
			.githubPullRequestUrl(task.getGithubPullRequestUrl())
			.createdAt(task.getCreatedAt())
			.updatedAt(task.getUpdatedAt())
			.createdBy(task.getCreatedBy() != null ? task.getCreatedBy().getName() : null)
			.updatedBy(task.getUpdatedBy() != null ? task.getUpdatedBy().getName() : null)
			.build();
	}
}
