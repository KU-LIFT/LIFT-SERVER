package com.kulift.lift.domain.project.controller;

import static com.kulift.lift.domain.project.entity.ProjectRole.*;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kulift.lift.domain.project.aop.RequireProjectRole;
import com.kulift.lift.domain.project.dto.ProjectCreateRequest;
import com.kulift.lift.domain.project.dto.ProjectResponse;
import com.kulift.lift.domain.project.dto.ProjectUpdateRequest;
import com.kulift.lift.domain.project.service.ProjectMemberService;
import com.kulift.lift.domain.project.service.ProjectService;
import com.kulift.lift.global.security.CustomUserDetails;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

	private final ProjectService projectService;
	private final ProjectMemberService projectMemberService;

	@PostMapping
	public ResponseEntity<ProjectResponse> createProject(@RequestBody @Valid ProjectCreateRequest request,
		@AuthenticationPrincipal CustomUserDetails userDetails) {
		return ResponseEntity.ok(projectService.createProject(request, userDetails.getId()));
	}

	@GetMapping
	public ResponseEntity<List<ProjectResponse>> getUserProjects(
		@AuthenticationPrincipal CustomUserDetails userDetails) {
		return ResponseEntity.ok(projectMemberService.getUserProjects(userDetails.getId()));
	}

	@RequireProjectRole(MEMBER)
	@GetMapping("/{projectKey}")
	public ResponseEntity<ProjectResponse> fetchProject(@PathVariable String projectKey) {
		return ResponseEntity.ok(projectService.getProjectByKey(projectKey));
	}

	@RequireProjectRole(MANAGER)
	@PutMapping("/{projectKey}")
	public ResponseEntity<ProjectResponse> updateProject(@PathVariable String projectKey,
		@RequestBody @Valid ProjectUpdateRequest request) {
		return ResponseEntity.ok(projectService.updateProject(projectKey, request));
	}

	@RequireProjectRole(OWNER)
	@DeleteMapping("/{projectKey}")
	public ResponseEntity<Void> deleteProject(@PathVariable String projectKey) {
		projectService.deleteProject(projectKey);
		return ResponseEntity.noContent().build();
	}
}
