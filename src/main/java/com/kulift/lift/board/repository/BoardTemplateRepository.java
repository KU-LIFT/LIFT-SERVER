package com.kulift.lift.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kulift.lift.board.entity.BoardTemplate;

public interface BoardTemplateRepository extends JpaRepository<BoardTemplate, Long> {
	boolean existsByName(String name);
}
