package com.kulift.lift.dto;

import com.kulift.lift.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserResponseDto {
    private final String name;
    private final String email;

    private UserResponseDto(User user) {
        this.name = user.getName();
        this.email = user.getEmail();
    }

    public static UserResponseDto from(User user) {
        return new UserResponseDto(user);
    }
}
