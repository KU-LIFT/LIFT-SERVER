package com.kulift.lift.task.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.kulift.lift.task.entity.Priority;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskRequest {
	@NotBlank
	private String name;

	private String description;

	@NotNull
	private Long columnId;

	@NotNull
	private Long assigneeId;

	@NotNull
	private Priority priority;

	private LocalDateTime dueDate;

	private List<String> tags;
}
