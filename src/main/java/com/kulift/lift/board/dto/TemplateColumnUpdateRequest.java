package com.kulift.lift.board.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TemplateColumnUpdateRequest {
	@NotBlank
	private String name;

	@PositiveOrZero
	private int position;
}
