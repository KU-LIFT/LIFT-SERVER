package com.kulift.lift.task.controller;

import static com.kulift.lift.project.entity.ProjectRole.*;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kulift.lift.auth.security.CustomUserDetails;
import com.kulift.lift.project.aop.RequireProjectRole;
import com.kulift.lift.task.dto.TaskColumnUpdateRequest;
import com.kulift.lift.task.dto.TaskRequest;
import com.kulift.lift.task.dto.TaskResponse;
import com.kulift.lift.task.service.TaskService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/projects/{projectKey}/tasks")
@RequiredArgsConstructor
public class TaskController {

	private final TaskService taskService;

	@RequireProjectRole(MEMBER)
	@PostMapping
	public ResponseEntity<TaskResponse> createTask(@RequestBody @Valid TaskRequest request,
		@AuthenticationPrincipal CustomUserDetails userDetails) {
		return ResponseEntity.ok(taskService.createTask(request, userDetails.getId()));
	}

	@RequireProjectRole(MEMBER)
	@GetMapping
	public ResponseEntity<List<TaskResponse>> getTasks() {
		return ResponseEntity.ok(taskService.getAllTasks());
	}

	@RequireProjectRole(MEMBER)
	@GetMapping("/{taskId}")
	public ResponseEntity<TaskResponse> getTask(@PathVariable Long taskId) {
		return ResponseEntity.ok(taskService.getTask(taskId));
	}

	@RequireProjectRole(MEMBER)
	@PutMapping("/{taskId}")
	public ResponseEntity<TaskResponse> updateTask(@PathVariable Long taskId, @RequestBody @Valid TaskRequest request,
		@AuthenticationPrincipal CustomUserDetails userDetails) {
		return ResponseEntity.ok(taskService.updateTask(taskId, request, userDetails.getId()));
	}

	@PatchMapping("/{taskId}/column")
	public ResponseEntity<TaskResponse> moveTaskToColumn(@PathVariable Long taskId,
		@RequestBody @Validated TaskColumnUpdateRequest request) {
		return ResponseEntity.ok(taskService.moveTaskToColumn(taskId, request.getColumnId()));
	}

	@RequireProjectRole(MEMBER)
	@DeleteMapping("/{taskId}")
	public ResponseEntity<Void> deleteTask(@PathVariable Long taskId) {
		taskService.deleteTask(taskId);
		return ResponseEntity.noContent().build();
	}
}
