package com.kulift.lift.domain.project.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kulift.lift.domain.auth.entity.User;
import com.kulift.lift.domain.auth.repository.UserRepository;
import com.kulift.lift.domain.board.service.BoardService;
import com.kulift.lift.domain.project.dto.ProjectCreateRequest;
import com.kulift.lift.domain.project.dto.ProjectResponse;
import com.kulift.lift.domain.project.dto.ProjectUpdateRequest;
import com.kulift.lift.domain.project.entity.Project;
import com.kulift.lift.domain.project.entity.ProjectRole;
import com.kulift.lift.domain.project.repository.ProjectRepository;
import com.kulift.lift.global.exception.CustomException;
import com.kulift.lift.global.exception.ErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjectService {

	private final ProjectRepository projectRepository;
	private final UserRepository userRepository;
	private final ProjectMemberService projectMemberService;
	private final BoardService boardService;

	@Transactional
	public ProjectResponse createProject(ProjectCreateRequest request, Long userId) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

		if (projectRepository.existsByProjectKey(request.getProjectKey())) {
			throw new CustomException(ErrorCode.DUPLICATE_PROJECT_KEY);
		}

		Project project = Project.builder()
			.projectKey(request.getProjectKey())
			.name(request.getName())
			.description(request.getDescription())
			.createdAt(LocalDateTime.now())
			.build();

		Project savedProject = projectRepository.save(project);
		projectMemberService.addMember(savedProject, user, ProjectRole.OWNER);

		return ProjectResponse.from(savedProject, boardService.createDefaultBoard(request.getProjectKey()));
	}

	@Transactional(readOnly = true)
	public List<ProjectResponse> getAllProjects() {
		return projectRepository.findAll().stream()
			.map(ProjectResponse::from)
			.collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public ProjectResponse getProjectByKey(String projectKey) {
		return ProjectResponse.from(findProjectByKey(projectKey));
	}

	@Transactional
	public ProjectResponse updateProject(String projectKey, ProjectUpdateRequest request) {
		Project project = findProjectByKey(projectKey);
		project.update(request.getName(), request.getDescription());
		return ProjectResponse.from(project);
	}

	@Transactional
	public void deleteProject(String projectKey) {
		projectMemberService.deleteAllByProjectKey(projectKey);
		projectRepository.deleteByProjectKey(projectKey);
	}

	public Project findProjectByKey(String projectKey) {
		return projectRepository.findByProjectKey(projectKey)
			.orElseThrow(() -> new CustomException(ErrorCode.PROJECT_NOT_FOUND));
	}
}
