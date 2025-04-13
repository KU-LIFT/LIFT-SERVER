package com.kulift.lift.user.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kulift.lift.auth.security.CustomUserDetails;
import com.kulift.lift.global.response.ApiResponse;
import com.kulift.lift.user.dto.PasswordUpdateRequest;
import com.kulift.lift.user.dto.UserResponse;
import com.kulift.lift.user.entity.User;
import com.kulift.lift.user.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@GetMapping("/me")
	public ApiResponse<UserResponse> getMyInfo(Authentication authentication) {
		User user = userService.findByEmail(authentication.getName());
		return ApiResponse.ok(UserResponse.from(user));
	}

	@GetMapping
	public ApiResponse<List<UserResponse>> getAllUsers() {
		return ApiResponse.ok(
			userService.findAll().stream()
				.map(UserResponse::from)
				.toList()
		);
	}

	@GetMapping("/{id}")
	public ApiResponse<UserResponse> getUserById(@PathVariable Long id) {
		return ApiResponse.ok(UserResponse.from(userService.findById(id)));
	}

	@PatchMapping("/me")
	public ApiResponse<String> updateUserPassword(
		@RequestBody @Valid PasswordUpdateRequest request,
		Authentication authentication
	) {
		CustomUserDetails userDetails = (CustomUserDetails)authentication.getPrincipal();
		userService.updatePassword(userDetails.getId(), request.password());
		return ApiResponse.ok("비밀번호가 성공적으로 변경되었습니다.");
	}

	@DeleteMapping("/{id}")
	public ApiResponse<String> deleteUser(@PathVariable Long id) {
		userService.delete(id);
		return ApiResponse.ok("삭제되었습니다.");
	}
}
