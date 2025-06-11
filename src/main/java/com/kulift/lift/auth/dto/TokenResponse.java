package com.kulift.lift.auth.dto;

public record TokenResponse(
	String accessToken,
	String refreshToken
) {
	public static TokenResponse of(String accessToken, String refreshToken) {
		return new TokenResponse(accessToken, refreshToken);
	}

	public static TokenResponse of(String accessToken) {
		return new TokenResponse(accessToken, null);
	}
}
