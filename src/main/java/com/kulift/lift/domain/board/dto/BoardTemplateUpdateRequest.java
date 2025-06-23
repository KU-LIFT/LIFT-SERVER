package com.kulift.lift.domain.board.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardTemplateUpdateRequest {
	@NotBlank
	private String name;

	@NotEmpty
	private List<TemplateColumnCreateRequest> columns;
}
