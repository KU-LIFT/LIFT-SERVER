package com.kulift.lift.board.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BoardCreateRequest {
	@NotBlank
	private String name;
	private Long templateId;
}
