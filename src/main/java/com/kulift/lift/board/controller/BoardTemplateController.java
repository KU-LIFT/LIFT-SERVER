package com.kulift.lift.board.controller;

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

import com.kulift.lift.board.dto.BoardTemplateRequest;
import com.kulift.lift.board.dto.BoardTemplateResponse;
import com.kulift.lift.board.service.BoardTemplateService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/boards/templates")
@RequiredArgsConstructor
public class BoardTemplateController {

	private final BoardTemplateService service;

	@GetMapping
	public ResponseEntity<List<BoardTemplateResponse>> getAllBoardTemplates() {
		return ResponseEntity.ok(service.getAllBoardTemplates());
	}

	@GetMapping("/{templateId}")
	public ResponseEntity<BoardTemplateResponse> getBoardTemplateById(
		@PathVariable("templateId") Long templateId) {
		return ResponseEntity.ok(service.getBoardTemplateById(templateId));
	}

	@PostMapping
	public ResponseEntity<BoardTemplateResponse> createBoardTemplate(
		@RequestBody @Valid BoardTemplateRequest request) {
		return ResponseEntity.ok(service.createBoardTemplate(request));
	}

	@PutMapping("/{templateId}")
	public ResponseEntity<BoardTemplateResponse> updateBoardTemplate(
		@PathVariable("templateId") Long templateId,
		@RequestBody @Valid BoardTemplateRequest request) {
		return ResponseEntity.ok(service.updateBoardTemplate(templateId, request));
	}

	@DeleteMapping("/{templateId}")
	public ResponseEntity<Void> deleteBoardTemplate(
		@PathVariable("templateId") Long templateId) {
		service.deleteBoardTemplate(templateId);
		return ResponseEntity.noContent().build();
	}
}
