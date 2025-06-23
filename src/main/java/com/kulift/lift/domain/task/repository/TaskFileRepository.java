package com.kulift.lift.domain.task.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kulift.lift.domain.task.entity.TaskFile;

public interface TaskFileRepository extends JpaRepository<TaskFile, Long> {
	List<TaskFile> findByTaskId(Long taskId);
}
