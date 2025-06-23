package com.kulift.lift.domain.board.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardUpdateRequest {
	@NotBlank
	private String name;
}
