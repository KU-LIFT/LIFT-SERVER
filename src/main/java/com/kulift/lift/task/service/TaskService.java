package com.kulift.lift.task.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kulift.lift.board.entity.BoardColumn;
import com.kulift.lift.board.service.BoardColumnService;
import com.kulift.lift.global.exception.CustomException;
import com.kulift.lift.global.exception.ErrorCode;
import com.kulift.lift.task.dto.TaskRequest;
import com.kulift.lift.task.dto.TaskResponse;
import com.kulift.lift.task.entity.Task;
import com.kulift.lift.task.repository.TaskRepository;
import com.kulift.lift.user.entity.User;
import com.kulift.lift.user.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TaskService {

	private final TaskRepository taskRepository;
	private final BoardColumnService columnService;
	private final UserService userService;

	@Transactional
	public TaskResponse createTask(TaskRequest request, Long userId) {
		BoardColumn column = columnService.findColumnById(request.getColumnId());
		User assignee = userService.findById(request.getAssigneeId());
		User creator = userService.findById(userId);

		Task task = Task.builder()
			.name(request.getName())
			.description(request.getDescription())
			.column(column)
			.assignee(assignee)
			.priority(request.getPriority())
			.dueDate(request.getDueDate())
			.tags(request.getTags())
			.createdBy(creator)
			.createdAt(LocalDateTime.now())
			.build();

		column.addTask(task);
		taskRepository.save(task);
		return TaskResponse.from(task);
	}

	public List<TaskResponse> getAllTasks() {
		return taskRepository.findAll().stream()
			.map(TaskResponse::from)
			.collect(Collectors.toList());
	}

	public TaskResponse getTask(Long taskId) {
		return TaskResponse.from(findTaskById(taskId));
	}

	@Transactional
	public TaskResponse updateTask(Long taskId, TaskRequest request, Long userId) {
		Task task = findTaskById(taskId);
		User assignee = userService.findById(request.getAssigneeId());
		User updater = userService.findById(userId);

		moveTaskToColumn(taskId, request.getColumnId());

		task.setName(request.getName());
		task.setDescription(request.getDescription());
		task.setAssignee(assignee);
		task.setPriority(request.getPriority());
		task.setDueDate(request.getDueDate());
		task.setTags(request.getTags());
		task.setUpdatedBy(updater);
		task.setUpdatedAt(LocalDateTime.now());

		return TaskResponse.from(task);
	}

	@Transactional
	public TaskResponse moveTaskToColumn(Long taskId, Long newColumnId) {
		Task task = findTaskById(taskId);
		task.setColumn(columnService.findColumnById(newColumnId));
		return TaskResponse.from(task);
	}

	@Transactional
	public void deleteTask(Long taskId) {
		Task task = taskRepository.findById(taskId)
			.orElseThrow(() -> new CustomException(ErrorCode.TASK_NOT_FOUND));
		taskRepository.delete(task);
	}

	public Task findTaskById(Long taskId) {
		return taskRepository.findById(taskId)
			.orElseThrow(() -> new CustomException(ErrorCode.TASK_NOT_FOUND));
	}
}
