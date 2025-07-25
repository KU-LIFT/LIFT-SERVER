package com.kulift.lift.domain.board.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TemplateColumnCreateRequest {
	@NotBlank
	private String name;

	@PositiveOrZero
	private int position;
}
