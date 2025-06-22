package com.kulift.lift.task.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kulift.lift.auth.security.CustomUserDetails;
import com.kulift.lift.task.dto.TaskResponse;
import com.kulift.lift.task.service.TaskService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskUserController {

	private final TaskService taskService;

	@GetMapping("/assigned")
	public ResponseEntity<List<TaskResponse>> getRecentUserTasks(
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@RequestParam(required = false) Integer limit) {
		return ResponseEntity.ok(taskService.getRecentUserTasks(userDetails.getId(), limit));
	}
}
