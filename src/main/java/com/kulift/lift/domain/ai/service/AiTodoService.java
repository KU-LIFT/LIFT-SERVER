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
			.maxCompletionTokens(1000)
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
