package com.kulift.lift.user.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String provider; // LOCAL, GOOGLE, GITHUB 등

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    public void updatePassword(String encodedPassword) {
        this.password = encodedPassword;
    }
}
