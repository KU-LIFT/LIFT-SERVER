package com.kulift.lift.domain.task.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kulift.lift.domain.task.entity.TaskComment;

public interface TaskCommentRepository extends JpaRepository<TaskComment, Long> {
	List<TaskComment> findByTaskId(Long taskId);
}
