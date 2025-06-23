package com.kulift.lift.domain.board.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kulift.lift.domain.board.dto.BoardCreateRequest;
import com.kulift.lift.domain.board.dto.BoardResponse;
import com.kulift.lift.domain.board.dto.BoardUpdateRequest;
import com.kulift.lift.domain.board.entity.Board;
import com.kulift.lift.domain.board.entity.BoardColumn;
import com.kulift.lift.domain.board.entity.BoardTemplate;
import com.kulift.lift.domain.board.repository.BoardRepository;
import com.kulift.lift.domain.board.repository.BoardTemplateRepository;
import com.kulift.lift.global.exception.CustomException;
import com.kulift.lift.global.exception.ErrorCode;
import com.kulift.lift.domain.project.entity.Project;
import com.kulift.lift.domain.project.repository.ProjectRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardService {

	private final BoardRepository boardRepository;
	private final ProjectRepository projectRepository;
	private final BoardTemplateRepository templateRepository;

	@Transactional
	public BoardResponse createBoard(String projectKey, BoardCreateRequest request) {
		Project project = findProjectByKey(projectKey);
		Board board = Board.create(request.getName(), project);
		applyTemplate(board, request.getTemplateId());
		boardRepository.save(board);
		return BoardResponse.from(board);
	}

	@Transactional
	public BoardResponse createDefaultBoard(String projectKey) {
		BoardCreateRequest boardRequest = new BoardCreateRequest(projectKey + " Board", 1L);
		return createBoard(projectKey, boardRequest);
	}

	public List<BoardResponse> getBoardsByProjectKey(String projectKey) {
		Project project = findProjectByKey(projectKey);
		return boardRepository.findByProject(project).stream()
			.map(BoardResponse::from)
			.collect(Collectors.toList());
	}

	public BoardResponse getBoardById(Long boardId) {
		Board board = findBoardById(boardId);
		return BoardResponse.from(board);
	}

	public Long findFirstBoardIdByProjectKey(String projectKey) {
		return boardRepository.findFirstByProjectKeyOrderByIdAsc(projectKey)
			.map(Board::getId)
			.orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));
	}

	@Transactional
	public BoardResponse updateBoard(Long boardId, BoardUpdateRequest request) {
		Board board = findBoardById(boardId);
		board.setName(request.getName());
		return BoardResponse.from(board);
	}

	@Transactional
	public void deleteBoard(Long boardId) {
		Board board = findBoardById(boardId);
		boardRepository.delete(board);
	}

	public Board findBoardById(Long boardId) {
		return boardRepository.findById(boardId)
			.orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));
	}

	public Long findLeftmostColumnId(Long boardId) {
		Board board = findBoardById(boardId);
		List<BoardColumn> cols = board.getColumns();
		if (cols.isEmpty()) {
			throw new CustomException(ErrorCode.COLUMN_NOT_FOUND);
		}
		return cols.getFirst().getId();
	}

	private void applyTemplate(Board board, Long templateId) {
		if (templateId == null) {
			return;
		}
		BoardTemplate tpl = templateRepository.findById(templateId)
			.orElseThrow(() -> new CustomException(ErrorCode.BOARD_TEMPLATE_NOT_FOUND));
		for (String colName : tpl.getTemplateColumns()) {
			BoardColumn col = BoardColumn.create(colName, board);
			board.getColumns().add(col);
		}
	}

	private Project findProjectByKey(String projectKey) {
		return projectRepository.findByProjectKey(projectKey)
			.orElseThrow(() -> new CustomException(ErrorCode.PROJECT_NOT_FOUND));
	}
}
