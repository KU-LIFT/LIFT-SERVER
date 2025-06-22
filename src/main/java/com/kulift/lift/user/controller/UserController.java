package com.kulift.lift.user.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kulift.lift.auth.security.CustomUserDetails;
import com.kulift.lift.task.dto.TaskResponse;
import com.kulift.lift.task.service.TaskService;
import com.kulift.lift.user.dto.PasswordUpdateRequest;
import com.kulift.lift.user.dto.UserResponse;
import com.kulift.lift.user.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;
	private final TaskService taskService;

	@GetMapping("/me")
	public ResponseEntity<UserResponse> getMyInfo(@AuthenticationPrincipal CustomUserDetails userDetails) {
		return ResponseEntity.ok(UserResponse.from(userService.findById(userDetails.getId())));
	}

	@GetMapping
	public ResponseEntity<List<UserResponse>> getAllUsers() {
		List<UserResponse> users = userService.findAll().stream().map(UserResponse::from).toList();
		return ResponseEntity.ok(users);
	}

	@GetMapping("/{id}")
	public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
		return ResponseEntity.ok(UserResponse.from(userService.findById(id)));
	}

	@PatchMapping("/me")
	public ResponseEntity<String> updateUserPassword(@RequestBody @Valid PasswordUpdateRequest request,
		@AuthenticationPrincipal CustomUserDetails userDetails) {
		userService.updatePassword(userDetails.getId(), request.password());
		return ResponseEntity.ok("비밀번호가 성공적으로 변경되었습니다.");
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteUser(@PathVariable Long id) {
		userService.delete(id);
		return ResponseEntity.ok("삭제되었습니다.");
	}

	// 프로젝트 키를 필요로 하지 않아서 TaskController 에 넣지 않았음.
	@GetMapping("/assigned")
	public ResponseEntity<List<TaskResponse>> getUserTasks(@AuthenticationPrincipal CustomUserDetails userDetails) {
		return ResponseEntity.ok(taskService.getUserTasks(userDetails.getId()));
	}
}
