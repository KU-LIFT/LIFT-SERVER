package com.kulift.lift.auth.jwt;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.kulift.lift.user.entity.User;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtTokenProvider {

	@Value("${jwt.secret}")
	private String secret;
	private Key key;

	@PostConstruct
	public void init() {
		key = Keys.hmacShaKeyFor(secret.getBytes());
	}

	public String getEmail(String token) {
		return Jwts.parserBuilder().setSigningKey(key).build()
			.parseClaimsJws(token)
			.getBody().getSubject();
	}

	public boolean validateToken(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
			return true;
		} catch (JwtException | IllegalArgumentException e) {
			log.warn("Invalid JWT token: {}", e.getMessage());
			return false;
		}
	}

	public String resolveToken(HttpServletRequest request) {
		String bearer = request.getHeader("Authorization");
		if (bearer != null && bearer.startsWith("Bearer ")) {
			return bearer.substring(7);
		}
		return null;
	}

	public String createAccessToken(User user) {
		return createToken(user, 1000 * 60 * 15); // 15분
	}

	public String createRefreshToken(User user) {
		return createToken(user, 1000 * 60 * 60 * 24 * 7); // 7일
	}

	public long getExpiration(String token) {
		return Jwts.parserBuilder().setSigningKey(key).build()
			.parseClaimsJws(token)
			.getBody().getExpiration().getTime();
	}

	private String createToken(User user, long validityMs) {
		return Jwts.builder()
			.setSubject(user.getEmail())
			.claim("role", user.getRole())
			.claim("provider", user.getProvider())
			.setIssuedAt(new Date())
			.setExpiration(new Date(System.currentTimeMillis() + validityMs))
			.signWith(key, SignatureAlgorithm.HS256)
			.compact();
	}

}
