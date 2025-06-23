package com.kulift.lift.domain.project.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ProjectCreateRequest {

	@NotBlank
	private String projectKey;

	@NotBlank
	private String name;

	private String description;
}
