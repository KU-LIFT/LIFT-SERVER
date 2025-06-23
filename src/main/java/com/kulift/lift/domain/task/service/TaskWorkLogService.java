package com.kulift.lift.domain.task.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kulift.lift.domain.auth.entity.User;
import com.kulift.lift.domain.auth.repository.UserRepository;
import com.kulift.lift.domain.task.dto.TaskWorkLogRequest;
import com.kulift.lift.domain.task.dto.TaskWorkLogResponse;
import com.kulift.lift.domain.task.entity.Task;
import com.kulift.lift.domain.task.entity.TaskWorkLog;
import com.kulift.lift.domain.task.repository.TaskWorkLogRepository;
import com.kulift.lift.global.exception.CustomException;
import com.kulift.lift.global.exception.ErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TaskWorkLogService {

	private final TaskWorkLogRepository workLogRepository;
	private final TaskService taskService;
	private final UserRepository userRepository;

	public List<TaskWorkLogResponse> getWorkLogs(Long taskId) {
		taskService.findTaskById(taskId);
		return workLogRepository.findByTaskId(taskId).stream()
			.map(TaskWorkLogResponse::from)
			.collect(Collectors.toList());
	}

	@Transactional
	public TaskWorkLogResponse addWorkLog(Long taskId, TaskWorkLogRequest request, Long userId) {
		Task task = taskService.findTaskById(taskId);
		User author = userRepository.findById(userId)
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
		TaskWorkLog workLog = TaskWorkLog.builder()
			.task(task)
			.durationMinutes(request.getDurationMinutes())
			.description(request.getDescription())
			.loggedBy(author)
			.build();
		workLogRepository.save(workLog);
		return TaskWorkLogResponse.from(workLog);
	}

	@Transactional
	public TaskWorkLogResponse updateWorkLog(Long workLogId, TaskWorkLogRequest req, Long userId) {
		TaskWorkLog log = workLogRepository.findById(workLogId)
			.orElseThrow(() -> new CustomException(ErrorCode.TASK_NOT_FOUND));
		if (!log.getLoggedBy().getId().equals(userId)) {
			throw new CustomException(ErrorCode.FORBIDDEN);
		}
		log.updateLog(req.getDurationMinutes(), req.getDescription());
		return TaskWorkLogResponse.from(log);
	}

	@Transactional
	public void deleteWorkLog(Long workLogId) {
		if (!workLogRepository.existsById(workLogId)) {
			throw new CustomException(ErrorCode.TASK_NOT_FOUND);
		}
		workLogRepository.deleteById(workLogId);
	}
}
