package com.kulift.lift.domain.project.dto;

import com.kulift.lift.domain.project.entity.ProjectMember;
import com.kulift.lift.domain.project.entity.ProjectRole;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProjectMemberResponse {
	private Long id;
	private String email;
	private String name;
	private ProjectRole role;

	public static ProjectMemberResponse from(ProjectMember member) {
		return ProjectMemberResponse.builder()
			.id(member.getUser().getId())
			.email(member.getUser().getEmail())
			.name(member.getUser().getName())
			.role(member.getRole())
			.build();
	}
}
