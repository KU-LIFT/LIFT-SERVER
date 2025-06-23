package com.kulift.lift.global.oauth2;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.kulift.lift.domain.auth.entity.Role;
import com.kulift.lift.domain.auth.entity.User;
import com.kulift.lift.domain.auth.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

	private final UserRepository userRepository;

	@Override
	public OAuth2User loadUser(OAuth2UserRequest request) {
		log.info
			("소셜 로그인 시작");

		OAuth2User oAuth2User = super.loadUser(request);
		Map<String, Object> attributes = oAuth2User.getAttributes();
		String provider = request.getClientRegistration().getRegistrationId();
		String accessToken = request.getAccessToken().getTokenValue();

		log.info
			("provider: {}", provider);
		log.info
			("attributes: {}", attributes);

		String email = extractEmail(provider, attributes, accessToken);
		String name = extractName(provider, attributes);

		log.info
			("email 추출 결과: {}", email);
		log.info
			("name 추출 결과: {}", name);

		User user = userRepository.findByEmail(email)
			.orElseGet(() -> {
				log.info
					("기존 유저 없음. 신규 생성");
				return userRepository.save(User.builder()
					.email(email)
					.name(name)
					.password("") // OAuth 사용자는 비밀번호 없음
					.provider(provider.toUpperCase())
					.role(Role.USER)
					.build());
			});

		log.info
			("최종 사용자 인증 완료: {}", user.getEmail());
		Map<String, Object> modifiableAttributes = new HashMap<>(attributes);
		modifiableAttributes.put("email", email);

		return new DefaultOAuth2User(
			List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name())),
			modifiableAttributes,
			"email"
		);
	}

	private String extractName(String provider, Map<String, Object> attributes) {
		if (provider.equals("google")) {
			return attributes.get("name").toString();
		}
		if (provider.equals("github")) {
			Object name = attributes.get("name");
			if (name != null)
				return name.toString();
			return attributes.get("login").toString(); // fallback
		}
		return "unknown";
	}

	private String extractEmail(String provider, Map<String, Object> attributes, String accessToken) {
		if (provider.equals("google")) {
			Object email = attributes.get("email");
			log.info
				("Google 프로필 email: {}", email);
			return email.toString();
		}

		if (provider.equals("github")) {
			Object email = attributes.get("email");
			if (email != null) {
				log.info
					("GitHub attributes에서 직접 email 획득: {}", email);
				return email.toString();
			}

			log.info
				("GitHub attributes에 email 없음 → /user/emails 호출 시작");

			// fallback to /user/emails
			HttpHeaders headers = new HttpHeaders();
			headers.setBearerAuth(accessToken);
			headers.setAccept(List.of(MediaType.APPLICATION_JSON));
			HttpEntity<Void> entity = new HttpEntity<>(headers);

			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
				"https://api.github.com/user/emails",
				HttpMethod.GET, entity, new ParameterizedTypeReference<>() {
				}
			);

			log.info
				("/user/emails API 응답: {}", response.getBody());

			return response.getBody().stream()
				.filter(e -> Boolean.TRUE.equals(e.get("primary")) && Boolean.TRUE.equals(e.get("verified")))
				.map(e -> e.get("email").toString())
				.findFirst()
				.orElseThrow(() -> new RuntimeException("GitHub 이메일을 가져올 수 없습니다."));
		}

		throw new RuntimeException("지원하지 않는 소셜 로그인 제공자입니다.");
	}
}
