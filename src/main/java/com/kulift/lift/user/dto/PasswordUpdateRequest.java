package com.kulift.lift.user.dto;

import jakarta.validation.constraints.NotBlank;

public record PasswordUpdateRequest(
        @NotBlank String password
) {
}
