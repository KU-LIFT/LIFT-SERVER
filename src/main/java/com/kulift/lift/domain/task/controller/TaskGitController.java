package com.kulift.lift.domain.task.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kulift.lift.domain.task.dto.CreatePrRequest;
import com.kulift.lift.domain.task.dto.GitPullRequestDto;
import com.kulift.lift.domain.task.service.TaskGitService;
import com.kulift.lift.global.git.dto.BranchInfoDto;
import com.kulift.lift.global.git.dto.CommitDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/projects/{projectKey}/tasks/{taskId}/git")
@RequiredArgsConstructor
public class TaskGitController {

	private final TaskGitService taskGitService;

	@PostMapping("/branch")
	public ResponseEntity<Void> createBranch(
		@PathVariable String projectKey,
		@PathVariable Long taskId,
		@RequestParam("newBranch") String newBranchName,
		@RequestParam(value = "baseBranch", required = false) String baseBranch
	) {
		taskGitService.createBranchAndLinkToTask(projectKey, taskId, newBranchName, baseBranch);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/branches")
	public ResponseEntity<List<BranchInfoDto>> listBranches(@PathVariable String projectKey,
		@PathVariable Long taskId) {
		List<BranchInfoDto> branches = taskGitService.listBranchesForTask(projectKey, taskId);
		return ResponseEntity.ok(branches);
	}

	@GetMapping("/commits")
	public ResponseEntity<List<CommitDto>> listCommits(@PathVariable String projectKey, @PathVariable Long taskId) {
		List<CommitDto> commits = taskGitService.listCommitsForTask(projectKey, taskId);
		return ResponseEntity.ok(commits);
	}

	@PostMapping("/pr")
	public ResponseEntity<GitPullRequestDto> createPullRequest(@PathVariable String projectKey,
		@PathVariable Long taskId,
		@RequestBody CreatePrRequest request
	) {
		GitPullRequestDto pr = taskGitService.createPullRequestForTask(projectKey, taskId, request.getTitle(),
			request.getBody());
		return ResponseEntity.ok(pr);
	}

	@GetMapping("/prs")
	public ResponseEntity<List<GitPullRequestDto>> listPullRequests(@PathVariable String projectKey,
		@PathVariable Long taskId) {
		List<GitPullRequestDto> prs = taskGitService.listPullRequestsForTask(projectKey, taskId);
		return ResponseEntity.ok(prs);
	}
}
