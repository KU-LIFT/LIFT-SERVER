package com.kulift.lift.user.dto;

import com.kulift.lift.user.entity.Role;
import com.kulift.lift.user.entity.User;

public record UserResponse(
        Long id,
        String name,
        String email,
        Role role,
        String provider
) {
    public static UserResponse from(User user) {
        return new UserResponse(user.getId(), user.getName(), user.getEmail(), user.getRole(), user.getProvider());
    }
}
