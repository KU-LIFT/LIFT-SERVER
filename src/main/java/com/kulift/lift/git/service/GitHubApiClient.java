package com.kulift.lift.git.service;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GitHubApiClient {

	private final WebClient webClient = WebClient.builder()
		.baseUrl("https://api.github.com")
		.defaultHeader(HttpHeaders.ACCEPT, "application/vnd.github+json")
		.build();

	public String getInstallationAccessToken(String jwt, long installationId) {
		return webClient.post()
			.uri("/app/installations/{installation_id}/access_tokens", installationId)
			.header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
			.retrieve()
			.bodyToMono(String.class)
			.block();
	}
}
