package com.kulift.lift.board.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kulift.lift.board.dto.BoardTemplateRequest;
import com.kulift.lift.board.dto.BoardTemplateResponse;
import com.kulift.lift.board.entity.BoardTemplate;
import com.kulift.lift.board.repository.BoardTemplateRepository;
import com.kulift.lift.global.exception.CustomException;
import com.kulift.lift.global.exception.ErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardTemplateService {

	private final BoardTemplateRepository repository;

	public List<BoardTemplateResponse> getAllBoardTemplates() {
		return repository.findAll().stream()
			.map(BoardTemplateResponse::from)
			.collect(Collectors.toList());
	}

	public BoardTemplateResponse getBoardTemplateById(Long templateId) {
		BoardTemplate template = findById(templateId);
		return BoardTemplateResponse.from(template);
	}

	@Transactional
	public BoardTemplateResponse createBoardTemplate(BoardTemplateRequest request) {
		if (repository.existsByName(request.getName())) {
			throw new CustomException(ErrorCode.DUPLICATE_TEMPLATE_NAME);
		}
		BoardTemplate template = BoardTemplate.builder()
			.name(request.getName())
			.templateColumns(request.getTemplateColumns())
			.build();
		repository.save(template);
		return BoardTemplateResponse.from(template);
	}

	@Transactional
	public BoardTemplateResponse updateBoardTemplate(Long templateId, BoardTemplateRequest request) {
		BoardTemplate template = findById(templateId);
		if (!template.getName().equals(request.getName())
			&& repository.existsByName(request.getName())) {
			throw new CustomException(ErrorCode.DUPLICATE_TEMPLATE_NAME);
		}
		template.setName(request.getName());
		template.setTemplateColumns(request.getTemplateColumns());
		return BoardTemplateResponse.from(template);
	}

	@Transactional
	public void deleteBoardTemplate(Long templateId) {
		repository.delete(findById(templateId));
	}

	private BoardTemplate findById(Long templateId) {
		return repository.findById(templateId)
			.orElseThrow(() -> new CustomException(ErrorCode.BOARD_TEMPLATE_NOT_FOUND));
	}
}
