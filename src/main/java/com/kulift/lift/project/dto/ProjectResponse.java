package com.kulift.lift.project.dto;

import java.time.LocalDateTime;

import com.kulift.lift.board.dto.BoardResponse;
import com.kulift.lift.project.entity.Project;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProjectResponse {
	private String projectKey;
	private String name;
	private String description;
	private LocalDateTime createdAt;

	private BoardResponse board;

	public static ProjectResponse from(Project project) {
		return ProjectResponse.builder()
			.projectKey(project.getProjectKey())
			.name(project.getName())
			.description(project.getDescription())
			.createdAt(project.getCreatedAt())
			.build();
	}

	public static ProjectResponse from(Project project, BoardResponse board) {
		return ProjectResponse.builder()
			.projectKey(project.getProjectKey())
			.name(project.getName())
			.description(project.getDescription())
			.createdAt(project.getCreatedAt())
			.board(board)
			.build();
	}
}
