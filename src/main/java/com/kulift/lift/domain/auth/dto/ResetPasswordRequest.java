package com.kulift.lift.domain.auth.dto;

public record ResetPasswordRequest(String token, String newPassword) {
}
