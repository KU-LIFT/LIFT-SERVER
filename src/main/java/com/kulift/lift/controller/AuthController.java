package com.kulift.lift.controller;

import com.kulift.lift.dto.SignupRequestDto;
import com.kulift.lift.dto.UserResponseDto;
import com.kulift.lift.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody @Valid SignupRequestDto request) {
        userService.registerUser(request);
        return ResponseEntity.ok("회원가입 성공!");
    }

    @GetMapping("/users")
    public List<UserResponseDto> getAllUsers() {
        return userService.getAllUsers();
    }
}
