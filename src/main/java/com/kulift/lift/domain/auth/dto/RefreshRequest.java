package com.kulift.lift.domain.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RefreshRequest(
	@Email @NotBlank String email,
	@NotBlank String refreshToken
) {
}
