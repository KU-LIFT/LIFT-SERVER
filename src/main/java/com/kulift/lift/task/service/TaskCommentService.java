package com.kulift.lift.task.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kulift.lift.global.exception.CustomException;
import com.kulift.lift.global.exception.ErrorCode;
import com.kulift.lift.task.dto.TaskCommentRequest;
import com.kulift.lift.task.dto.TaskCommentResponse;
import com.kulift.lift.task.entity.Task;
import com.kulift.lift.task.entity.TaskComment;
import com.kulift.lift.task.repository.TaskCommentRepository;
import com.kulift.lift.user.entity.User;
import com.kulift.lift.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TaskCommentService {

	private final TaskCommentRepository commentRepository;
	private final TaskService taskService;
	private final UserRepository userRepository;

	public List<TaskCommentResponse> getComments(Long taskId) {
		Task task = taskService.findTaskById(taskId);
		return commentRepository.findByTaskId(task.getId()).stream()
			.map(TaskCommentResponse::from)
			.collect(Collectors.toList());
	}

	@Transactional
	public TaskCommentResponse addComment(Long taskId, TaskCommentRequest request, Long userId) {
		Task task = taskService.findTaskById(taskId);
		User author = userRepository.findById(userId)
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
		TaskComment comment = TaskComment.builder()
			.task(task)
			.content(request.getContent())
			.createdBy(author)
			.build();
		commentRepository.save(comment);
		return TaskCommentResponse.from(comment);
	}

	@Transactional
	public TaskCommentResponse updateComment(Long commentId, TaskCommentRequest req, Long userId) {
		TaskComment comment = commentRepository.findById(commentId)
			.orElseThrow(() -> new CustomException(ErrorCode.TASK_NOT_FOUND));
		if (!comment.getCreatedBy().getId().equals(userId)) {
			throw new CustomException(ErrorCode.FORBIDDEN);
		}
		comment.updateContent(req.getContent());
		return TaskCommentResponse.from(comment);
	}

	@Transactional
	public void deleteComment(Long commentId) {
		if (!commentRepository.existsById(commentId)) {
			throw new CustomException(ErrorCode.TASK_NOT_FOUND);
		}
		commentRepository.deleteById(commentId);
	}
}
