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

		String accessToken = tokenService.getInstallationAccessToken(installationId);

		JsonNode repoList = githubClient.get()
			.uri("/installation/repositories")
			.header(HttpHeaders.AUTHORIZATION, "token " + accessToken)
			.retrieve()
			.bodyToMono(JsonNode.class)
			.block();

		if (repoList == null
			|| !repoList.has("repositories")
			|| repoList.get("repositories").isEmpty()) {
			throw new CustomException(ErrorCode.GITHUB_REPO_NOT_FOUND);
		}

		JsonNode firstRepo = repoList.get("repositories").get(0);
		String owner = firstRepo.get("owner").get("login").asText();
		String name = firstRepo.get("name").asText();
		String defaultBranch = firstRepo.get("default_branch").asText();

		project.connectGithubRepository(installationId, owner, name, defaultBranch);
	}
}
