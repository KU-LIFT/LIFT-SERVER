package com.kulift.lift.user.service;

import com.kulift.lift.global.exception.CustomException;
import com.kulift.lift.global.exception.ErrorCode;
import com.kulift.lift.user.entity.Role;
import com.kulift.lift.user.entity.User;
import com.kulift.lift.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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
        userRepository.deleteById(id);
    }
}
