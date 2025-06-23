package com.kulift.lift.domain.project.dto;

import com.kulift.lift.domain.project.entity.ProjectRole;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ProjectMemberInviteRequest {
	@Email
	private String email;

	@NotNull
	private ProjectRole role;
}
