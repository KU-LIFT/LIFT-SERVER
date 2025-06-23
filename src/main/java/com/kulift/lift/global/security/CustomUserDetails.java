package com.kulift.lift.global.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.kulift.lift.domain.auth.entity.User;

import lombok.Getter;

@Getter
public class CustomUserDetails implements UserDetails {

	private final Long id;
	private final String email;
	private final String password;
	private final String provider;
	private final Collection<? extends GrantedAuthority> authorities;

	public CustomUserDetails(User user) {
		this.id = user.getId();
		this.email = user.getEmail();
		this.password = user.getPassword();
		this.provider = user.getProvider();
		this.authorities = List.of(() -> "ROLE_" + user.getRole().name());
	}

	@Override
	public String getUsername() {
		return email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
