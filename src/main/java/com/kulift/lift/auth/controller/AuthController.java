package com.kulift.lift.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kulift.lift.auth.dto.LoginRequest;
import com.kulift.lift.auth.dto.RefreshRequest;
import com.kulift.lift.auth.dto.SignupRequest;
import com.kulift.lift.auth.dto.TokenResponse;
import com.kulift.lift.auth.jwt.JwtTokenProvider;
import com.kulift.lift.auth.jwt.RefreshTokenService;
import com.kulift.lift.user.entity.User;
import com.kulift.lift.user.service.UserService;

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

	@PostMapping("/signup")
	public ResponseEntity<String> signup(@RequestBody @Valid SignupRequest req) {
		User user = userService.register(req.name(), req.email(), req.password());
		return ResponseEntity.ok("회원가입 성공: " + user.getEmail());
	}

	@PostMapping("/login")
	public ResponseEntity<TokenResponse> login(@RequestBody @Valid LoginRequest req) {
		User user = userService.findByEmail(req.email());
		if (!passwordEncoder.matches(req.password(), user.getPassword())) {
			throw new RuntimeException("비밀번호가 일치하지 않습니다");
		}
		String accessToken = jwtTokenProvider.createAccessToken(user);
		String refreshToken = jwtTokenProvider.createRefreshToken(user);
		refreshTokenService.save(user.getEmail(), refreshToken);
		return ResponseEntity.ok(new TokenResponse(accessToken, refreshToken));
	}

	@PostMapping("/token/refresh")
	public ResponseEntity<String> refresh(@RequestBody RefreshRequest req) {
		if (!refreshTokenService.isValid(req.email(), req.refreshToken())) {
			throw new RuntimeException("Refresh Token이 유효하지 않습니다");
		}
		User user = userService.findByEmail(req.email());
		String newAccessToken = jwtTokenProvider.createAccessToken(user);
		return ResponseEntity.ok(newAccessToken);
	}
}
