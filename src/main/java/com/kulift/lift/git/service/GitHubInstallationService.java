package com.kulift.lift.git.service;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.kulift.lift.global.exception.CustomException;
import com.kulift.lift.global.exception.ErrorCode;
import com.kulift.lift.project.entity.Project;
import com.kulift.lift.project.repository.ProjectRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GitHubInstallationService {

	private final GitHubAppTokenService tokenService;
	private final ProjectRepository projectRepository;

	private final WebClient githubClient = WebClient.builder()
		.baseUrl("https://api.github.com")
		.defaultHeader(HttpHeaders.ACCEPT, "application/vnd.github+json")
		.build();

	@Transactional
	public void linkInstallationToProject(String projectKey, Long installationId) {
		Project project = projectRepository.findByProjectKey(projectKey)
			.orElseThrow(() -> new CustomException(ErrorCode.PROJECT_NOT_FOUND));

		String jwt = tokenService.generateAppJwtToken();

		// installation access token 요청
		String accessToken = githubClient.post()
			.uri("/app/installations/{installation_id}/access_tokens", installationId)
			.header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
			.retrieve()
			.bodyToMono(JsonNode.class)
			.block()
			.get("token").asText();

		// 설치된 레포지토리 조회
		JsonNode repo = githubClient.get()
			.uri("/installation/repositories")
			.header(HttpHeaders.AUTHORIZATION, "token " + accessToken)
			.retrieve()
			.bodyToMono(JsonNode.class)
			.block()
			.get("repositories").get(0);

		String owner = repo.get("owner").get("login").asText();
		String name = repo.get("name").asText();
		String branch = repo.get("default_branch").asText();

		project.linkGitHubRepository(installationId, owner, name, branch);
	}
}
