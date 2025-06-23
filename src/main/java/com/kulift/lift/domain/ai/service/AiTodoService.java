package com.kulift.lift.domain.ai.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.JsonNode;
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
			다음 문서에서 '할 일 목록(TODO)'을 JSON 배열 형식으로 추출해주세요.
			
			**요구 사항을 반드시 지켜주세요:**
			1. 결과는 **반드시 JSON 배열 형태**로 출력하세요. **JSON 외에는 아무 것도 출력하지 마세요.**
			2. 각 항목은 다음 2개의 필드를 가져야 합니다:
			   - `"name"`: 할 일 제목 (짧고 핵심적으로 작성)
			   - `"description"`: 해당 할 일의 상세 설명 (**최소 3문장 이상, 문단 형태로 작성**)
			3. 최소 6개 이상의 할 일 항목을 추출해주세요.
			4. JSON 형식은 **정확하게** 지켜주세요. 중괄호, 대괄호, 쌍따옴표 누락 없이 완전한 JSON이어야 합니다.
			5. 설명은 중복 없이 구체적으로 작성해주세요.
			
			예시:
			[
			  {
			    "name": "사용자 인증 기능 구현",
			    "description": "사용자가 이메일과 비밀번호로 로그인할 수 있도록 인증 기능을 구현합니다. JWT 토큰을 사용해 보안을 강화합니다. 로그인 실패 시 적절한 에러 메시지를 제공해야 합니다."
			  },
			  {
			    "name": "게시판 CRUD 기능 개발",
			    "description": "사용자는 게시글을 작성, 조회, 수정, 삭제할 수 있어야 합니다. 게시글은 제목, 본문, 작성일자, 작성자로 구성됩니다. 데이터베이스 모델 설계와 API 구현이 필요합니다."
			  }
			]
			
			아래 문서를 참고해 작업 목록을 작성해주세요:
			
			""" + documentText;

		ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
			.addUserMessage(prompt)
			.model(ChatModel.GPT_3_5_TURBO)
			.temperature(0.2)
			.maxCompletionTokens(2048)
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
			.orElse("")
			.trim();

		if (content.isBlank()) {
			log.warn("AI가 빈 문자열 반환");
			return List.of();
		}

		String validJson = trimToValidJsonArray(content);
		if (validJson == null) {
			log.error("유효한 JSON 배열을 추출하지 못함: {}", content);
			throw new CustomException(ErrorCode.INTERNAL_ERROR);
		}

		try {
			JsonNode root = objectMapper.readTree(validJson);
			List<TodoItem> items = new ArrayList<>();
			if (root.isArray()) {
				for (JsonNode node : root) {
					if (node.has("name") && node.has("description")) {
						items.add(new TodoItem(
							node.get("name").asText(),
							node.get("description").asText()
						));
					}
				}
			}
			return items;
		} catch (IOException e) {
			log.error("AI 응답 파싱 실패: {}", validJson, e);
			throw new CustomException(ErrorCode.INTERNAL_ERROR);
		}
	}

	// JSON 배열의 유효한 부분만 잘라내기
	private String trimToValidJsonArray(String raw) {
		int depth = 0;
		int lastIndex = -1;
		for (int i = 0; i < raw.length(); i++) {
			char c = raw.charAt(i);
			if (c == '{')
				depth++;
			else if (c == '}') {
				depth--;
				if (depth == 0)
					lastIndex = i;
			}
		}
		if (lastIndex != -1) {
			String trimmed = raw.substring(0, lastIndex + 1);
			// 배열 앞뒤 추출
			int arrayStart = trimmed.indexOf('[');
			return arrayStart != -1 ? trimmed.substring(arrayStart) + "]" : null;
		}
		return null;
	}

	public String extractTextFromFile(MultipartFile file) throws IOException {
		return documentService.extractText(file);
	}

	public record TodoItem(String name, String description) {
	}
}
