package com.kulift.lift.domain.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kulift.lift.domain.auth.dto.LoginRequest;
import com.kulift.lift.domain.auth.dto.ResetPasswordRequest;
import com.kulift.lift.domain.auth.dto.SignupRequest;
import com.kulift.lift.domain.auth.dto.TokenResponse;
import com.kulift.lift.domain.auth.entity.User;
import com.kulift.lift.domain.auth.service.ForgotPasswordService;
import com.kulift.lift.domain.auth.service.UserService;
import com.kulift.lift.global.exception.CustomException;
import com.kulift.lift.global.exception.ErrorCode;
import com.kulift.lift.global.security.JwtTokenProvider;
import com.kulift.lift.global.security.RefreshTokenService;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

	private final UserService userService;
	private final JwtTokenProvider jwtTokenProvider;
	private final PasswordEncoder passwordEncoder;
	private final RefreshTokenService refreshTokenService;
	private final ForgotPasswordService forgotPasswordService;

	@PostMapping("/signup")
	public ResponseEntity<String> signup(@RequestBody @Valid SignupRequest req) {
		User user = userService.register(req.name(), req.email(), req.password());
		return ResponseEntity.ok("회원가입 성공: " + user.getEmail());
	}

	@PostMapping("/login")
	public ResponseEntity<TokenResponse> login(@RequestBody @Valid LoginRequest req) {
		User user = userService.findByEmail(req.email());
		if (!passwordEncoder.matches(req.password(), user.getPassword())) {
			throw new CustomException(ErrorCode.INVALID_PASSWORD);
		}
		String accessToken = jwtTokenProvider.createAccessToken(user);
		String refreshToken = jwtTokenProvider.createRefreshToken(user);
		long refreshTokenExpiration = jwtTokenProvider.getExpiration(refreshToken);

		refreshTokenService.save(user.getEmail(), refreshToken, refreshTokenExpiration);
		return ResponseEntity.ok(TokenResponse.of(accessToken, refreshToken));
	}

	@PostMapping("/refresh")
	public ResponseEntity<TokenResponse> refresh(HttpServletRequest request) {
		String refreshToken = jwtTokenProvider.resolveToken(request);
		String email = jwtTokenProvider.getEmail(refreshToken);

		if (refreshToken == null || !jwtTokenProvider.validateToken(refreshToken) || !refreshTokenService.isValid(email,
			refreshToken)) {
			throw new CustomException(ErrorCode.INVALID_TOKEN);
		}

		User user = userService.findByEmail(email);
		String newAccessToken = jwtTokenProvider.createAccessToken(user);
		return ResponseEntity.ok(TokenResponse.of(newAccessToken));
	}

	@PostMapping("/forgot-password")
	public ResponseEntity<Void> forgotPassword(@RequestParam String email) throws MessagingException {
		forgotPasswordService.sendResetLink(email);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/reset-password")
	public ResponseEntity<Void> resetPassword(@RequestBody ResetPasswordRequest request) {
		forgotPasswordService.resetPassword(request);
		return ResponseEntity.ok().build();
	}
}
