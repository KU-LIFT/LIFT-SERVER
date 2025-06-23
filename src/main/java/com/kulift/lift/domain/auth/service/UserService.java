package com.kulift.lift.domain.auth.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.kulift.lift.domain.auth.entity.Role;
import com.kulift.lift.domain.auth.entity.User;
import com.kulift.lift.domain.auth.repository.UserRepository;
import com.kulift.lift.domain.project.repository.ProjectMemberRepository;
import com.kulift.lift.global.exception.CustomException;
import com.kulift.lift.global.exception.ErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final ProjectMemberRepository projectMemberRepository;

	public User register(String name, String email, String rawPassword) {
		if (userRepository.existsByEmail(email)) {
			throw new CustomException(ErrorCode.VALIDATION_ERROR);
		}
		User user = User.builder()
			.name(name)
			.email(email)
			.password(passwordEncoder.encode(rawPassword))
			.provider("LOCAL")
			.role(Role.USER)
			.build();

		return userRepository.save(user);
	}

	public User findById(Long id) {
		return userRepository.findById(id)
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
	}

	public User findByEmail(String email) {
		return userRepository.findByEmail(email)
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
	}

	public List<User> findAll() {
		return userRepository.findAll();
	}

	public void updatePassword(Long userId, String newPassword) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
		user.updatePassword(passwordEncoder.encode(newPassword));
		userRepository.save(user);
	}

	public void delete(Long id) {
		projectMemberRepository.deleteByUserId(id);
		userRepository.deleteById(id);
	}
}
