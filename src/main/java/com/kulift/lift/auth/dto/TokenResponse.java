package com.kulift.lift.auth.dto;

public record TokenResponse(
        String accessToken,
        String refreshToken
) {
}
