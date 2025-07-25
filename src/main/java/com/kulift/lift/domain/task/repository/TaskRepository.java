package com.kulift.lift.domain.task.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kulift.lift.domain.task.entity.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {
	List<Task> findByColumnId(Long columnId);

	Optional<Task> findByIdAndColumnId(Long id, Long columnId);

	List<Task> findByAssigneeId(Long assigneeId);
}
