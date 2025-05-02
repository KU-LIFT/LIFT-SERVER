package com.kulift.lift.task.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kulift.lift.task.entity.TaskWorkLog;

public interface TaskWorkLogRepository extends JpaRepository<TaskWorkLog, Long> {
    List<TaskWorkLog> findByTaskId(Long taskId);
}