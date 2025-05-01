package com.kulift.lift.board.controller;

import static com.kulift.lift.project.entity.ProjectRole.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kulift.lift.board.dto.ColumnOrderRequest;
import com.kulift.lift.board.dto.ColumnRequest;
import com.kulift.lift.board.dto.ColumnResponse;
import com.kulift.lift.board.service.BoardColumnService;
import com.kulift.lift.project.aop.RequireProjectRole;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/projects/{projectKey}/boards/{boardId}/columns")
@RequiredArgsConstructor
public class BoardColumnController {

	private final BoardColumnService columnService;

	@RequireProjectRole(MEMBER)
	@PostMapping
	public ResponseEntity<ColumnResponse> addColumn(
		@PathVariable Long boardId,
		@RequestBody @Valid ColumnRequest request
	) {
		return ResponseEntity.ok(columnService.addColumn(boardId, request));
	}

	@RequireProjectRole(MEMBER)
	@PutMapping("/{columnId}")
	public ResponseEntity<ColumnResponse> updateColumn(
		@PathVariable Long boardId,
		@PathVariable Long columnId,
		@RequestBody @Valid ColumnRequest request
	) {
		return ResponseEntity.ok(columnService.updateColumn(boardId, columnId, request));
	}

	@RequireProjectRole(MEMBER)
	@PatchMapping("/reorder")
	public ResponseEntity<Void> reorderColumns(
		@PathVariable Long boardId,
		@RequestBody @Valid ColumnOrderRequest request
	) {
		columnService.reorderColumns(boardId, request.getOrderedIds());
		return ResponseEntity.noContent().build();
	}

	@RequireProjectRole(MEMBER)
	@DeleteMapping("/{columnId}")
	public ResponseEntity<Void> deleteColumn(
		@PathVariable Long boardId,
		@PathVariable Long columnId
	) {
		columnService.deleteColumn(boardId, columnId);
		return ResponseEntity.noContent().build();
	}
}
