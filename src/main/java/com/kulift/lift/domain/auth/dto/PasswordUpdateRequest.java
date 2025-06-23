package com.kulift.lift.domain.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record PasswordUpdateRequest(
	@NotBlank String password
) {
}
