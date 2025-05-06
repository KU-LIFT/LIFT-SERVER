package com.kulift.lift.ai.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;

@Configuration
public class OpenAIConfig {

	@Value("${openai.api-key}")
	private String apiKey;

	@Bean
	public OpenAIClient openAIClient() {
		return OpenAIOkHttpClient.builder()
			.apiKey(apiKey)      // ← 여기서 직접 API Key 주입
			.build();
	}
}
