package com.kulift.lift.domain.task.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kulift.lift.domain.project.entity.Project;
import com.kulift.lift.domain.project.service.ProjectService;
import com.kulift.lift.domain.task.dto.GitPullRequestDto;
import com.kulift.lift.domain.task.entity.Task;
import com.kulift.lift.global.exception.CustomException;
import com.kulift.lift.global.exception.ErrorCode;
import com.kulift.lift.global.git.dto.BranchInfoDto;
import com.kulift.lift.global.git.dto.CommitDto;
import com.kulift.lift.global.git.dto.PullRequestDto;
import com.kulift.lift.global.git.service.GitHubApiClient;
import com.kulift.lift.global.git.service.GitHubAppTokenService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskGitService {

	private final TaskService taskService;
	private final ProjectService projectService;
	private final GitHubAppTokenService tokenService;
	private final GitHubApiClient gitHubApiClient;

	@Transactional
	public void createBranchAndLinkToTask(String projectKey, Long taskId, String newBranchName, String baseBranch) {
		Task task = taskService.findTaskById(taskId);

		Project project = projectService.findProjectByKey(projectKey);

		Long installationId = project.getGithubInstallationId();
		if (installationId == null) {
			throw new CustomException(ErrorCode.GITHUB_INSTALLATION_NOT_FOUND);
		}

		String token = tokenService.getInstallationAccessToken(installationId);
		String owner = project.getGithubRepoOwner();
		String repo = project.getGithubRepoName();

		String chosenBase =
			(baseBranch != null && !baseBranch.isBlank()) ? baseBranch : project.getGithubDefaultBranch();

		BranchInfoDto branchInfo = gitHubApiClient.getBranchInfo(token, owner, repo, chosenBase);
		String baseSha = branchInfo.getCommitSha();

		gitHubApiClient.createBranch(token, owner, repo, newBranchName, baseSha);

		task.linkGitInfo(newBranchName, null, baseSha);
	}

	@Transactional(readOnly = true)
	public List<BranchInfoDto> listBranchesForTask(String projectKey, Long taskId) {
		Task task = taskService.findTaskById(taskId);
		String branchName = task.getGithubBranch();
		if (branchName == null) {
			return List.of();
		}

		Project project = projectService.findProjectByKey(projectKey);
		Long installationId = project.getGithubInstallationId();
		if (installationId == null) {
			throw new CustomException(ErrorCode.GITHUB_INSTALLATION_NOT_FOUND);
		}

		String token = tokenService.getInstallationAccessToken(installationId);
		BranchInfoDto branch = gitHubApiClient.getBranchInfo(
			token, project.getGithubRepoOwner(), project.getGithubRepoName(), branchName);

		return List.of(branch);
	}

	@Transactional(readOnly = true)
	public List<CommitDto> listCommitsForTask(String projectKey, Long taskId) {
		Task task = taskService.findTaskById(taskId);

		String branch = task.getGithubBranch();
		if (branch == null) {
			return List.of();
		}

		Project project = projectService.findProjectByKey(projectKey);

		Long installationId = project.getGithubInstallationId();
		if (installationId == null) {
			throw new CustomException(ErrorCode.GITHUB_INSTALLATION_NOT_FOUND);
		}

		String token = tokenService.getInstallationAccessToken(installationId);
		String owner = project.getGithubRepoOwner();
		String repo = project.getGithubRepoName();

		return gitHubApiClient.listCommits(token, owner, repo, branch);
	}

	@Transactional
	public GitPullRequestDto createPullRequestForTask(String projectKey, Long taskId, String title, String body) {
		Task task = taskService.findTaskById(taskId);

		String headBranch = task.getGithubBranch();
		if (headBranch == null) {
			throw new CustomException(ErrorCode.GITHUB_BRANCH_ERROR);
		}

		Project project = projectService.findProjectByKey(projectKey);

		Long installationId = project.getGithubInstallationId();
		if (installationId == null) {
			throw new CustomException(ErrorCode.GITHUB_INSTALLATION_NOT_FOUND);
		}

		String token = tokenService.getInstallationAccessToken(installationId);
		String owner = project.getGithubRepoOwner();
		String repo = project.getGithubRepoName();
		String baseBranch = project.getGithubDefaultBranch();

		PullRequestDto prDto = gitHubApiClient.createPullRequest(
			token, owner, repo, headBranch, baseBranch, title, body
		);

		task.linkGitInfo(headBranch, prDto.getHtmlUrl(), prDto.getHead().getSha());

		return new GitPullRequestDto(prDto.getHtmlUrl(), prDto.getHead().getSha(), prDto.getState());
	}

	@Transactional(readOnly = true)
	public List<GitPullRequestDto> listPullRequestsForTask(String projectKey, Long taskId) {
		Task task = taskService.findTaskById(taskId);

		String headBranch = task.getGithubBranch();
		if (headBranch == null) {
			return List.of();
		}

		Project project = projectService.findProjectByKey(projectKey);

		Long installationId = project.getGithubInstallationId();
		if (installationId == null) {
			throw new CustomException(ErrorCode.GITHUB_INSTALLATION_NOT_FOUND);
		}

		String token = tokenService.getInstallationAccessToken(installationId);
		String owner = project.getGithubRepoOwner();
		String repo = project.getGithubRepoName();

		List<PullRequestDto> prList = gitHubApiClient.listPullRequests(
			token, owner, repo, headBranch
		);

		return prList.stream()
			.map(pr -> new GitPullRequestDto(pr.getHtmlUrl(), pr.getHead().getSha(), pr.getState()))
			.collect(Collectors.toList());
	}
}
