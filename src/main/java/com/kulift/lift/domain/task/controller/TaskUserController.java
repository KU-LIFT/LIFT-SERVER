package com.kulift.lift.domain.task.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kulift.lift.domain.task.dto.TaskResponse;
import com.kulift.lift.domain.task.service.TaskService;
import com.kulift.lift.global.security.CustomUserDetails;

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
