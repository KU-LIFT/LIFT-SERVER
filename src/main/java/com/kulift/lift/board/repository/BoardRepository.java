package com.kulift.lift.board.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kulift.lift.board.entity.Board;
import com.kulift.lift.project.entity.Project;

public interface BoardRepository extends JpaRepository<Board, Long> {
	List<Board> findByProject(Project project);

	Optional<Board> findByIdAndProject(Long id, Project project);

	Optional<Board> findFirstByProjectKeyOrderByIdAsc(String projectKey);
}
