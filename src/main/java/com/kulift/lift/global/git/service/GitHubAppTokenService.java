package com.kulift.lift.global.git.service;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.JsonNode;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class GitHubAppTokenService {

	private final WebClient webClient = WebClient.builder()
		.baseUrl("https://api.github.com")
		.defaultHeader(HttpHeaders.ACCEPT, "application/vnd.github.v3+json")
		.build();
	@Value("${github.app-id}")
	private String appId;
	@Value("${github.private-key-path}")
	private String pemFilePath;

	private RSAPrivateKey getPrivateKey() throws Exception {
		String pemContent = Files.readAllLines(Paths.get(pemFilePath)).stream()
			.filter(line -> !line.startsWith("-----"))
			.collect(java.util.stream.Collectors.joining());

		byte[] decodedKey = Base64.getDecoder().decode(pemContent);
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodedKey);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		return (RSAPrivateKey)keyFactory.generatePrivate(keySpec);
	}

	private String generateJwt() {
		try {
			PrivateKey privateKey = getPrivateKey();
			Instant now = Instant.now();
			Instant exp = now.plusSeconds(600); // 10분

			return Jwts.builder()
				.setIssuer(appId)
				.setIssuedAt(Date.from(now))
				.setExpiration(Date.from(exp))
				.signWith(privateKey, SignatureAlgorithm.RS256)
				.compact();
		} catch (Exception e) {
			throw new RuntimeException("GitHub JWT 생성 실패", e);
		}
	}

	public String getInstallationAccessToken(Long installationId) {
		String jwt = generateJwt();

		JsonNode response = webClient.post()
			.uri("/app/installations/{installation_id}/access_tokens", installationId)
			.header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
			.header(HttpHeaders.ACCEPT, "application/vnd.github.v3+json")
			.retrieve()
			.bodyToMono(JsonNode.class)
			.block();

		if (response == null || !response.has("token")) {
			throw new RuntimeException("GitHub Installation Access Token 발급 실패");
		}
		return response.get("token").asText();
	}
}
