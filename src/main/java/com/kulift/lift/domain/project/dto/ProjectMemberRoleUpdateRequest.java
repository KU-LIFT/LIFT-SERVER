package com.kulift.lift.domain.project.dto;

import com.kulift.lift.domain.project.entity.ProjectRole;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ProjectMemberRoleUpdateRequest {
	@NotNull
	private Long userId;
	@NotNull
	private ProjectRole role;
}
