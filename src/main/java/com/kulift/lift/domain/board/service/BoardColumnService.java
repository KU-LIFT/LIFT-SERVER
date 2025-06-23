package com.kulift.lift.domain.board.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kulift.lift.domain.board.dto.ColumnRequest;
import com.kulift.lift.domain.board.dto.ColumnResponse;
import com.kulift.lift.domain.board.entity.Board;
import com.kulift.lift.domain.board.entity.BoardColumn;
import com.kulift.lift.domain.board.repository.BoardColumnRepository;
import com.kulift.lift.global.exception.CustomException;
import com.kulift.lift.global.exception.ErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardColumnService {

	private final BoardColumnRepository columnRepository;
	private final BoardService boardService;

	@Transactional
	public ColumnResponse addColumn(Long boardId, ColumnRequest request) {
		Board board = boardService.findBoardById(boardId);
		BoardColumn column = BoardColumn.create(request.getName(), board);
		board.getColumns().add(column);
		columnRepository.save(column);
		return ColumnResponse.from(column);
	}

	public BoardColumn findColumnById(Long columnId) {
		return columnRepository.findById(columnId)
			.orElseThrow(() -> new CustomException(ErrorCode.COLUMN_NOT_FOUND));
	}

	@Transactional
	public ColumnResponse updateColumn(Long boardId, Long columnId, ColumnRequest request) {
		Board board = boardService.findBoardById(boardId);
		BoardColumn column = columnRepository.findByIdAndBoard(columnId, board)
			.orElseThrow(() -> new CustomException(ErrorCode.COLUMN_NOT_FOUND));
		column.setName(request.getName());
		return ColumnResponse.from(column);
	}

	@Transactional
	public void reorderColumns(Long boardId, List<Long> orderedIds) {
		List<BoardColumn> cols = columnRepository.findByBoardIdOrderByOrderIdx(boardId)
			.orElseThrow(() -> new CustomException(ErrorCode.COLUMN_NOT_FOUND));

		if (orderedIds.size() != cols.size()) {
			throw new CustomException(ErrorCode.INVALID_COLUMN_MOVE);
		}

		Map<Long, BoardColumn> map = new HashMap<>();
		for (BoardColumn c : cols) {
			map.put(c.getId(), c);
		}
		for (int i = 0; i < orderedIds.size(); i++) {
			BoardColumn c = map.get(orderedIds.get(i));
			if (c == null) {
				throw new CustomException(ErrorCode.INVALID_COLUMN_MOVE);
			}
			c.setOrderIdx(i);
		}
		columnRepository.saveAll(cols);
	}

	@Transactional
	public void deleteColumn(Long boardId, Long columnId) {
		Board board = boardService.findBoardById(boardId);
		BoardColumn column = columnRepository.findByIdAndBoard(columnId, board)
			.orElseThrow(() -> new CustomException(ErrorCode.COLUMN_NOT_FOUND));
		columnRepository.delete(column);
	}
}
