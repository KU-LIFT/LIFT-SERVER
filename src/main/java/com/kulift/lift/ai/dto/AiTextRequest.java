package com.kulift.lift.ai.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class AiTextRequest {
	@NotBlank
	private String text;
}
