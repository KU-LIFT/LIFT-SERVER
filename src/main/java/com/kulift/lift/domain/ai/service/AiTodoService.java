package com.kulift.lift.domain.ai.service;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kulift.lift.global.exception.CustomException;
import com.kulift.lift.global.exception.ErrorCode;
import com.openai.client.OpenAIClient;
import com.openai.models.ChatModel;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionCreateParams;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiTodoService {

	private final DocumentService documentService;
	private final OpenAIClient openAIClient;
	private final ObjectMapper objectMapper;

	public List<TodoItem> extractTodoItems(String documentText) {
		String prompt = """
			다음 문서에서 '할 일 목록(TODO)'을 뽑아 JSON 배열 형태로 출력해주세요.
			• 각 항목은 name, description 두 필드를 가진 객체로 작성
			• 배열 외에는 어떤 텍스트도 출력하지 마세요.
			예시: [{"name":"첫 번째","description":"설명1"},...]
			문서:
			""" + documentText;

		ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
			.addUserMessage(prompt)
			.model(ChatModel.GPT_3_5_TURBO)
			.temperature(0.0)
			.maxCompletionTokens(300)
			.build();

		ChatCompletion completion = openAIClient
			.chat()
			.completions()
			.create(params);

		var choices = completion.choices();
		if (choices.isEmpty()) {
			log.warn("AI 응답 없음, completion={}", completion);
			return List.of();
		}

		String content = choices.getFirst()
			.message()
			.content()
			.orElse("");
		if (content.isBlank()) {
			log.warn("AI가 빈 문자열 반환");
			return List.of();
		}

		try {
			return objectMapper.readValue(
				content.trim(),
				new TypeReference<List<TodoItem>>() {
				}
			);
		} catch (IOException e) {
			log.error("AI 응답 파싱 실패: {}", content, e);
			throw new CustomException(ErrorCode.INTERNAL_ERROR);
		}
	}

	public String extractTextFromFile(MultipartFile file) throws IOException {
		return documentService.extractText(file);
	}

	public record TodoItem(String name, String description) {
	}
}
