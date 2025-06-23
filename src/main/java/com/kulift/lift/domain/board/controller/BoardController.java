package com.kulift.lift.domain.board.controller;

import static com.kulift.lift.domain.project.entity.ProjectRole.*;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kulift.lift.domain.board.dto.BoardCreateRequest;
import com.kulift.lift.domain.board.dto.BoardResponse;
import com.kulift.lift.domain.board.dto.BoardUpdateRequest;
import com.kulift.lift.domain.board.service.BoardService;
import com.kulift.lift.domain.project.aop.RequireProjectRole;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/projects/{projectKey}/boards")
@RequiredArgsConstructor
public class BoardController {

	private final BoardService boardService;

	@RequireProjectRole(MEMBER)
	@GetMapping
	public ResponseEntity<List<BoardResponse>> getBoards(@PathVariable String projectKey) {
		return ResponseEntity.ok(boardService.getBoardsByProjectKey(projectKey));
	}

	@RequireProjectRole(MEMBER)
	@GetMapping("/{boardId}")
	public ResponseEntity<BoardResponse> getBoard(@PathVariable Long boardId) {
		return ResponseEntity.ok(boardService.getBoardById(boardId));
	}

	@RequireProjectRole(MANAGER)
	@PostMapping
	public ResponseEntity<BoardResponse> createBoard(@PathVariable String projectKey,
		@RequestBody @Valid BoardCreateRequest request) {
		return ResponseEntity.ok(boardService.createBoard(projectKey, request));
	}

	@RequireProjectRole(MANAGER)
	@PutMapping("/{boardId}")
	public ResponseEntity<BoardResponse> updateBoard(@PathVariable Long boardId,
		@RequestBody @Valid BoardUpdateRequest request) {
		return ResponseEntity.ok(boardService.updateBoard(boardId, request));
	}

	@RequireProjectRole(MANAGER)
	@DeleteMapping("/{boardId}")
	public ResponseEntity<Void> deleteBoard(@PathVariable Long boardId) {
		boardService.deleteBoard(boardId);
		return ResponseEntity.noContent().build();
	}
}
