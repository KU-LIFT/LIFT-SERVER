package com.kulift.lift.domain.project.controller;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kulift.lift.domain.project.entity.Project;
import com.kulift.lift.domain.project.service.ProjectService;
import com.kulift.lift.global.exception.CustomException;
import com.kulift.lift.global.exception.ErrorCode;
import com.kulift.lift.global.git.dto.BranchInfoDto;
import com.kulift.lift.global.git.service.GitHubApiClient;
import com.kulift.lift.global.git.service.GitHubAppTokenService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjectGitService {

	private final ProjectService projectService;
	private final GitHubAppTokenService tokenService;
	private final GitHubApiClient gitHubApiClient;

	@Transactional(readOnly = true)
	public List<BranchInfoDto> getBranches(String projectKey) {

		Project project = projectService.findProjectByKey(projectKey);

		Long installationId = project.getGithubInstallationId();
		if (installationId == null) {
			throw new CustomException(ErrorCode.GITHUB_INSTALLATION_NOT_FOUND);
		}

		String token = tokenService.getInstallationAccessToken(installationId);
		String owner = project.getGithubRepoOwner();
		String repo = project.getGithubRepoName();
		List<BranchInfoDto> branches = gitHubApiClient.listBranches(token, owner, repo);

		return branches != null ? branches : List.of();
	}

}
