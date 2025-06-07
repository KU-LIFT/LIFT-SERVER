package com.kulift.lift.global.config;

public final class SecurityWhitelistUrls {

	public static final String[] PERMIT_ALL = {
		/* ---------- auth ---------- */
		"/api/auth/**",
		"/oauth2/**",
		"/login/**",
		"/api/github/install/callback",
		/* ---------- Swagger ---------- */
		"/swagger-ui/**",
		"/v3/api-docs/**"
	};

	private SecurityWhitelistUrls() {
	}
}
