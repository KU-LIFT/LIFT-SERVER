package com.kulift.lift.domain.project.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kulift.lift.domain.board.service.BoardService;
import com.kulift.lift.global.git.service.GitHubInstallationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/github")
@RequiredArgsConstructor
public class GithubCallbackController {

	private final GitHubInstallationService installationService;
	private final BoardService boardService;

	@GetMapping("/install/callback")
	public ResponseEntity<Void> handleInstallationCallback(
		@RequestParam("installation_id") Long installationId,
		@RequestParam("state") String projectKey
	) {
		installationService.linkInstallationToProject(projectKey, installationId);
		Long boardId = boardService.findFirstBoardIdByProjectKey(projectKey);

		String redirectUrl = "http://kulift.com/boards/";

		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.LOCATION, redirectUrl);
		return new ResponseEntity<>(headers, HttpStatus.FOUND);
	}
}
