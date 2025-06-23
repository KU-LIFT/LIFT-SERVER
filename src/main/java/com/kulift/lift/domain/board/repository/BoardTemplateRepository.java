package com.kulift.lift.domain.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kulift.lift.domain.board.entity.BoardTemplate;

public interface BoardTemplateRepository extends JpaRepository<BoardTemplate, Long> {
	boolean existsByName(String name);
}
