package com.kulift.lift.domain.board.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ColumnRequest {
	@NotBlank
	private String name;
}
