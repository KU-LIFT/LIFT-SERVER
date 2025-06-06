package com.kulift.lift.project.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kulift.lift.git.service.GitHubInstallationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/github")
@RequiredArgsConstructor
public class ProjectGithubController {

	private final GitHubInstallationService installationService;

	@GetMapping("/install/callback")
	public ResponseEntity<String> handleInstallationCallback(
		@RequestParam("installation_id") Long installationId,
		@RequestParam("project") String projectKey
	) {
		installationService.linkInstallationToProject(projectKey, installationId);
		return ResponseEntity.ok("GitHub App successfully linked to project: " + projectKey);
	}
}
