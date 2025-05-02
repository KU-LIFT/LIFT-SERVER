package com.kulift.lift.task.controller;

import static com.kulift.lift.project.entity.ProjectRole.*;

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

import com.kulift.lift.auth.security.CustomUserDetails;
import com.kulift.lift.project.aop.RequireProjectRole;
import com.kulift.lift.task.dto.TaskWorkLogRequest;
import com.kulift.lift.task.dto.TaskWorkLogResponse;
import com.kulift.lift.task.service.TaskWorkLogService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/projects/{projectKey}/tasks/{taskId}/worklogs")
@RequiredArgsConstructor
public class TaskWorkLogController {

	private final TaskWorkLogService workLogService;

	@RequireProjectRole(MEMBER)
	@GetMapping
	public ResponseEntity<List<TaskWorkLogResponse>> getWorkLogs(@PathVariable Long taskId) {
		return ResponseEntity.ok(workLogService.getWorkLogs(taskId));
	}

	@RequireProjectRole(MEMBER)
	@PostMapping
	public ResponseEntity<TaskWorkLogResponse> addWorkLog(@PathVariable Long taskId,
		@RequestBody @Valid TaskWorkLogRequest request, @AuthenticationPrincipal CustomUserDetails userDetails) {
		return ResponseEntity.ok(workLogService.addWorkLog(taskId, request, userDetails.getId()));
	}

	@RequireProjectRole(MEMBER)
	@PutMapping("/{workLogId}")
	public ResponseEntity<TaskWorkLogResponse> updateWorkLog(
		@PathVariable Long workLogId,
		@RequestBody @Valid TaskWorkLogRequest request,
		@AuthenticationPrincipal CustomUserDetails userDetails
	) {
		return ResponseEntity.ok(
			workLogService.updateWorkLog(workLogId, request, userDetails.getId())
		);
	}

	@RequireProjectRole(MEMBER)
	@DeleteMapping("/{workLogId}")
	public ResponseEntity<Void> deleteWorkLog(@PathVariable Long workLogId) {
		workLogService.deleteWorkLog(workLogId);
		return ResponseEntity.noContent().build();
	}
}
