package com.kulift.lift.domain.task.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskCommentRequest {
	@NotBlank
	private String content;
}
