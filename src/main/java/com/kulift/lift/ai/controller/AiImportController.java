package com.kulift.lift.ai.controller;

import static com.kulift.lift.project.entity.ProjectRole.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.kulift.lift.ai.dto.AiTextRequest;
import com.kulift.lift.ai.service.AiTodoService;
import com.kulift.lift.auth.security.CustomUserDetails;
import com.kulift.lift.board.service.BoardService;
import com.kulift.lift.project.aop.RequireProjectRole;
import com.kulift.lift.task.dto.TaskRequest;
import com.kulift.lift.task.dto.TaskResponse;
import com.kulift.lift.task.entity.Priority;
import com.kulift.lift.task.service.TaskService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/projects/{projectKey}/boards/{boardId}/ai")
@RequiredArgsConstructor
public class AiImportController {

	private final AiTodoService aiTodoService;
	private final TaskService taskService;
	private final BoardService boardService;

	@RequireProjectRole(MEMBER)
	@PostMapping("/create-tasks")
	public ResponseEntity<List<TaskResponse>> importTodosFromText(
		@PathVariable Long boardId,
		@RequestBody AiTextRequest request,
		@AuthenticationPrincipal CustomUserDetails userDetails
	) {
		List<AiTodoService.TodoItem> items = aiTodoService.extractTodoItems(request.getText());
		return ResponseEntity.ok(createTasks(boardId, items, userDetails.getId()));
	}

	@RequireProjectRole(MEMBER)
	@PostMapping(value = "/create-tasks", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<List<TaskResponse>> importTodosFromFile(
		@PathVariable Long boardId,
		@RequestPart("file") MultipartFile file,
		@AuthenticationPrincipal CustomUserDetails userDetails
	) throws IOException {
		String text = aiTodoService.extractTextFromFile(file);
		List<AiTodoService.TodoItem> items = aiTodoService.extractTodoItems(text);
		return ResponseEntity.ok(createTasks(boardId, items, userDetails.getId()));
	}

	private List<TaskResponse> createTasks(Long boardId, List<AiTodoService.TodoItem> items, Long userId) {
		Long columnId = boardService.findLeftmostColumnId(boardId);
		return items.stream().map(item -> {
			TaskRequest req = new TaskRequest();
			req.setName(item.name());
			req.setDescription(item.description());
			req.setColumnId(columnId);
			req.setAssigneeId(userId);
			req.setPriority(Priority.MEDIUM);
			req.setDueDate(null);
			req.setTags(List.of("AI-import"));
			return taskService.createTask(req, userId);
		}).collect(Collectors.toList());
	}
}
