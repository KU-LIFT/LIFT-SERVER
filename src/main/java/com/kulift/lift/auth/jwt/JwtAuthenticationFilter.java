package com.kulift.lift.auth.jwt;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.kulift.lift.auth.security.CustomUserDetailsService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtTokenProvider jwtTokenProvider;
	private final CustomUserDetailsService userDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
		throws ServletException, IOException {

		String token = jwtTokenProvider.resolveToken(req);

		if (token != null && jwtTokenProvider.validateToken(token)) {
			String email = jwtTokenProvider.getEmail(token);
			log.info("토큰 유효. 사용자 이메일: {}", email);

			var userDetails = userDetailsService.loadUserByUsername(email);
			var auth = new UsernamePasswordAuthenticationToken(
				userDetails, null, userDetails.getAuthorities());
			SecurityContextHolder.getContext().setAuthentication(auth);

			log.info("인증 성공: {}", userDetails.getUsername());
		} else {
			log.warn("인증 실패. 토큰 유효하지 않음.");
		}

		chain.doFilter(req, res);
	}
}
