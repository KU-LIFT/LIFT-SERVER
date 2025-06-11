package com.kulift.lift.task.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskColumnUpdateRequest {

	@NotNull
	private Long columnId;
}
