package com.kulift.lift.git.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GitHubAppTokenService {

	@Value("${github.app-id}")
	private String appId;

	@Value("${github.private-key-path}")
	private String privateKeyPath;

	public String generateAppJwtToken() {
		try {
			String pem = Files.readString(Path.of(privateKeyPath));
			String base64 = pem.replace("-----BEGIN RSA PRIVATE KEY-----", "")
				.replace("-----END RSA PRIVATE KEY-----", "")
				.replaceAll("\\s+", "");

			byte[] keyBytes = Base64.getDecoder().decode(base64);
			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
			PrivateKey privateKey = KeyFactory.getInstance("RSA").generatePrivate(keySpec);

			Instant now = Instant.now();
			return Jwts.builder()
				.setIssuer(appId)
				.setIssuedAt(Date.from(now))
				.setExpiration(Date.from(now.plusSeconds(600)))
				.signWith(privateKey, SignatureAlgorithm.RS256)
				.compact();
		} catch (Exception e) {
			throw new RuntimeException("GitHub JWT 생성 실패", e);
		}
	}
}
