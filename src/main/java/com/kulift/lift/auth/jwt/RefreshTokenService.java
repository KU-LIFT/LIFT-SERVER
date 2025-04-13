package com.kulift.lift.auth.jwt;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RefreshTokenService {

    // email → refreshToken
    private final Map<String, String> refreshTokenStore = new ConcurrentHashMap<>();

    public void save(String email, String refreshToken) {
        refreshTokenStore.put(email, refreshToken);
    }

    public boolean isValid(String email, String refreshToken) {
        return refreshToken.equals(refreshTokenStore.get(email));
    }

    public void remove(String email) {
        refreshTokenStore.remove(email);
    }
}
