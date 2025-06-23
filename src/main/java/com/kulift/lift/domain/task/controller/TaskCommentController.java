package com.kulift.lift.domain.task.controller;

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
import com.kulift.lift.domain.task.dto.TaskCommentRequest;
import com.kulift.lift.domain.task.dto.TaskCommentResponse;
import com.kulift.lift.domain.task.service.TaskCommentService;
import com.kulift.lift.global.security.CustomUserDetails;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/projects/{projectKey}/tasks/{taskId}/comments")
@RequiredArgsConstructor
public class TaskCommentController {

	private final TaskCommentService commentService;

	@RequireProjectRole(MEMBER)
	@GetMapping
	public ResponseEntity<List<TaskCommentResponse>> getComments(@PathVariable Long taskId) {
		return ResponseEntity.ok(commentService.getComments(taskId));
	}

	@RequireProjectRole(MEMBER)
	@PostMapping
	public ResponseEntity<TaskCommentResponse> addComment(@PathVariable Long taskId,
		@RequestBody @Valid TaskCommentRequest request, @AuthenticationPrincipal CustomUserDetails userDetails) {
		return ResponseEntity.ok(commentService.addComment(taskId, request, userDetails.getId()));
	}

	@RequireProjectRole(MEMBER)
	@PutMapping("/{commentId}")
	public ResponseEntity<TaskCommentResponse> updateComment(
		@PathVariable Long commentId,
		@RequestBody @Valid TaskCommentRequest request,
		@AuthenticationPrincipal CustomUserDetails userDetails
	) {
		return ResponseEntity.ok(
			commentService.updateComment(commentId, request, userDetails.getId())
		);
	}

	@RequireProjectRole(MEMBER)
	@DeleteMapping("/{commentId}")
	public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
		commentService.deleteComment(commentId);
		return ResponseEntity.noContent().build();
	}
}
