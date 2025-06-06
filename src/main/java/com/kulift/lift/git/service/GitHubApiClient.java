package com.kulift.lift.git.service;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.kulift.lift.git.dto.BranchInfoDto;
import com.kulift.lift.git.dto.CommitDto;
import com.kulift.lift.git.dto.PullRequestDto;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class GitHubApiClient {

	private final WebClient webClient = WebClient.builder()
		.baseUrl("https://api.github.com")
		.defaultHeader(HttpHeaders.ACCEPT, "application/vnd.github+json")
		.build();

	public BranchInfoDto getBranchInfo(String token, String owner, String repo, String branch) {
		return webClient.get()
			.uri("/repos/{owner}/{repo}/branches/{branch}", owner, repo, branch)
			.header(HttpHeaders.AUTHORIZATION, "token " + token)
			.retrieve()
			.bodyToMono(BranchInfoDto.class)
			.block();
	}

	public List<BranchInfoDto> listBranches(String token, String owner, String repo) {
		return webClient.get()
			.uri("/repos/{owner}/{repo}/branches", owner, repo)
			.header(HttpHeaders.AUTHORIZATION, "token " + token)
			.retrieve()
			.bodyToFlux(BranchInfoDto.class)
			.collectList()
			.block();
	}

	public void createBranch(String token, String owner, String repo, String newBranch, String baseSha) {
		webClient.post()
			.uri("/repos/{owner}/{repo}/git/refs", owner, repo)
			.header(HttpHeaders.AUTHORIZATION, "token " + token)
			.bodyValue(Map.of(
				"ref", "refs/heads/" + newBranch,
				"sha", baseSha
			))
			.retrieve()
			.bodyToMono(Void.class)
			.block();
	}

	public List<CommitDto> listCommits(String token, String owner, String repo, String branch) {
		Flux<CommitDto> flux = webClient.get()
			.uri(uriBuilder ->
				uriBuilder.path("/repos/{owner}/{repo}/commits")
					.queryParam("sha", branch)
					.build(owner, repo))
			.header(HttpHeaders.AUTHORIZATION, "token " + token)
			.retrieve()
			.bodyToFlux(CommitDto.class);

		return flux.collectList().block();
	}

	public PullRequestDto createPullRequest(String token, String owner, String repo,
		String headBranch, String baseBranch,
		String title, String body) {
		return webClient.post()
			.uri("/repos/{owner}/{repo}/pulls", owner, repo)
			.header(HttpHeaders.AUTHORIZATION, "token " + token)
			.bodyValue(Map.of(
				"head", headBranch,
				"base", baseBranch,
				"title", title,
				"body", body
			))
			.retrieve()
			.bodyToMono(PullRequestDto.class)
			.block();
	}

	public List<PullRequestDto> listPullRequests(String token, String owner, String repo, String headBranch) {
		Flux<PullRequestDto> flux = webClient.get()
			.uri(uriBuilder ->
				uriBuilder.path("/repos/{owner}/{repo}/pulls")
					.queryParam("head", owner + ":" + headBranch)
					.build(owner, repo))
			.header(HttpHeaders.AUTHORIZATION, "token " + token)
			.retrieve()
			.bodyToFlux(PullRequestDto.class);

		return flux.collectList().block();
	}
}
