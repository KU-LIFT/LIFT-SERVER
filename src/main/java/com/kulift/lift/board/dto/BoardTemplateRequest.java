package com.kulift.lift.board.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BoardTemplateRequest {

	@NotBlank
	private String name;

	@NotEmpty
	private List<@NotBlank String> templateColumns;
}
