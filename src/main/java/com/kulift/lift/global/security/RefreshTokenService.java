package com.kulift.lift.global.security;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Service
public class RefreshTokenService {

	private final Map<String, StoredToken> refreshTokenStore = new ConcurrentHashMap<>();

	public void save(String email, String refreshToken, long expirationEpochMs) {
		refreshTokenStore.put(email, new StoredToken(refreshToken, expirationEpochMs));
	}

	public boolean isValid(String email, String refreshToken) {
		StoredToken stored = refreshTokenStore.get(email);
		if (stored == null || System.currentTimeMillis() > stored.expiration) {
			refreshTokenStore.remove(email); // 만료되었으면 바로 제거
			return false;
		}
		return stored.token.equals(refreshToken);
	}

	public void remove(String email) {
		refreshTokenStore.remove(email);
	}

	// 24시간마다 만료된 토큰 정리
	@Scheduled(fixedRate = 1000 * 60 * 60 * 24)
	public void clearExpiredTokens() {
		long now = System.currentTimeMillis();
		refreshTokenStore.entrySet().removeIf(entry -> entry.getValue().expiration < now);
	}

	@Getter
	@AllArgsConstructor
	private static class StoredToken {
		private final String token;
		private final long expiration;
	}
}
