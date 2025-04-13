package com.kulift.lift.auth.oauth.handler;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.kulift.lift.auth.jwt.JwtTokenProvider;
import com.kulift.lift.user.entity.User;
import com.kulift.lift.user.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

	private final JwtTokenProvider jwtTokenProvider;
	private final UserRepository userRepository;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request,
		HttpServletResponse response,
		Authentication authentication) throws IOException {
		OAuth2User oAuth2User = (OAuth2User)authentication.getPrincipal();
		String email = oAuth2User.getAttribute("email");

		User user = userRepository.findByEmail(email)
			.orElseThrow(() -> new RuntimeException("존재하지 않는 사용자입니다."));

		String token = jwtTokenProvider.createAccessToken(user);
		response.sendRedirect("http://kulift.com/oauth-success?token=" + token);
	}
}
