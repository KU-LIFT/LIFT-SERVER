package com.kulift.lift.project.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
	public ResponseEntity<Void> handleInstallationCallback(
		@RequestParam("installation_id") Long installationId,
		@RequestParam("state") String projectKey
	) {
		installationService.linkInstallationToProject(projectKey, installationId);

		// String frontendUrl = "https://kulift.com/projects/" + projectKey + "/settings?github=success";
		String frontendUrl = "http://localhost:5432/";

		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.LOCATION, frontendUrl);
		return new ResponseEntity<>(headers, HttpStatus.FOUND);
	}
}
