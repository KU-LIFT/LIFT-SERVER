package com.kulift.lift.domain.ai.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class AiTextRequest {
	@NotBlank
	private String text;
}
