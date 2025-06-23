package com.kulift.lift.domain.board.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kulift.lift.domain.board.entity.Board;
import com.kulift.lift.domain.board.entity.BoardColumn;

public interface BoardColumnRepository extends JpaRepository<BoardColumn, Long> {
	Optional<BoardColumn> findByIdAndBoard(Long id, Board board);

	Optional<List<BoardColumn>> findByBoardId(Long boardId);

	Optional<List<BoardColumn>> findByBoardIdOrderByOrderIdx(Long boardId);
}
