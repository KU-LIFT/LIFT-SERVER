package com.kulift.lift.domain.auth.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kulift.lift.domain.auth.dto.ResetPasswordRequest;
import com.kulift.lift.domain.auth.entity.PasswordResetToken;
import com.kulift.lift.domain.auth.entity.User;
import com.kulift.lift.domain.auth.repository.PasswordResetTokenRepository;
import com.kulift.lift.domain.auth.repository.UserRepository;
import com.kulift.lift.global.exception.CustomException;
import com.kulift.lift.global.exception.ErrorCode;
import com.kulift.lift.global.service.EmailService;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ForgotPasswordService {

	private final UserRepository userRepository;
	private final PasswordResetTokenRepository tokenRepository;
	private final PasswordEncoder passwordEncoder;
	private final EmailService emailService;

	@Transactional
	public void sendResetLink(String email) throws MessagingException {
		User user = userRepository.findByEmail(email)
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

		tokenRepository.deleteByUserId(user.getId());

		String token = UUID.randomUUID().toString();
		PasswordResetToken resetToken = PasswordResetToken.builder()
			.token(token)
			.user(user)
			.expiryDate(LocalDateTime.now().plusHours(1))
			.build();

		tokenRepository.save(resetToken);
		emailService.sendVerificationEmail(user.getEmail(), token);
	}

	@Transactional
	public void resetPassword(ResetPasswordRequest request) {
		PasswordResetToken token = tokenRepository.findByToken(request.token())
			.orElseThrow(() -> new CustomException(ErrorCode.INVALID_TOKEN));

		if (token.isExpired()) {
			throw new CustomException(ErrorCode.TOKEN_EXPIRED);
		}

		User user = token.getUser();
		user.updatePassword(passwordEncoder.encode(request.newPassword()));
		tokenRepository.delete(token);
	}
}
