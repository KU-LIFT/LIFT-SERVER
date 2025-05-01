package com.kulift.lift.project.controller;

import static com.kulift.lift.project.entity.ProjectRole.*;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kulift.lift.project.aop.RequireProjectRole;
import com.kulift.lift.project.dto.ProjectMemberInviteRequest;
import com.kulift.lift.project.dto.ProjectMemberResponse;
import com.kulift.lift.project.dto.ProjectMemberRoleUpdateRequest;
import com.kulift.lift.project.service.ProjectMemberService;
import com.kulift.lift.project.service.ProjectRoleService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/projects/{projectKey}/members")
@RequiredArgsConstructor
public class ProjectMemberController {

	private final ProjectMemberService projectMemberService;
	private final ProjectRoleService projectRoleService;

	@RequireProjectRole(MANAGER)
	@PostMapping
	public ResponseEntity<Void> inviteMember(@PathVariable String projectKey,
		@RequestBody @Valid ProjectMemberInviteRequest request) {
		projectMemberService.inviteMember(projectKey, request.getEmail(), request.getRole());
		return ResponseEntity.ok().build();
	}

	@RequireProjectRole(MEMBER)
	@GetMapping
	public ResponseEntity<List<ProjectMemberResponse>> getMembers(@PathVariable String projectKey) {
		return ResponseEntity.ok(projectMemberService.getProjectMembers(projectKey));
	}

	@RequireProjectRole(MANAGER)
	@PatchMapping("/role")
	public ResponseEntity<Void> updateRole(@PathVariable String projectKey,
		@RequestBody @Valid ProjectMemberRoleUpdateRequest request) {
		projectRoleService.updateRole(projectKey, request.getUserId(), request.getRole());
		return ResponseEntity.ok().build();
	}

	@RequireProjectRole(OWNER)
	@DeleteMapping("/{userId}")
	public ResponseEntity<Void> removeMember(@PathVariable String projectKey,
		@PathVariable Long userId) {
		projectMemberService.removeMember(projectKey, userId);
		return ResponseEntity.noContent().build();
	}
}
