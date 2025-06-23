package com.kulift.lift.domain.auth.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kulift.lift.domain.auth.dto.PasswordUpdateRequest;
import com.kulift.lift.domain.auth.dto.UserResponse;
import com.kulift.lift.domain.auth.service.UserService;
import com.kulift.lift.global.security.CustomUserDetails;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@GetMapping("/me")
	public ResponseEntity<UserResponse> getMyInfo(@AuthenticationPrincipal CustomUserDetails userDetails) {
		return ResponseEntity.ok(UserResponse.from(userService.findById(userDetails.getId())));
	}

	@PreAuthorize("hasRole('ADMIN')")
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

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteUser(@PathVariable Long id) {
		userService.delete(id);
		return ResponseEntity.ok("삭제되었습니다.");
	}

	@DeleteMapping
	public ResponseEntity<String> withdrawUser(@AuthenticationPrincipal CustomUserDetails userDetails) {
		userService.delete(userDetails.getId());
		return ResponseEntity.ok("회원 탈퇴 완료.");
	}
}
