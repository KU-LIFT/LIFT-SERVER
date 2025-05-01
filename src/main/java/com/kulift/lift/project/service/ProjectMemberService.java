package com.kulift.lift.project.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kulift.lift.global.exception.CustomException;
import com.kulift.lift.global.exception.ErrorCode;
import com.kulift.lift.project.dto.ProjectMemberResponse;
import com.kulift.lift.project.dto.ProjectResponse;
import com.kulift.lift.project.entity.Project;
import com.kulift.lift.project.entity.ProjectMember;
import com.kulift.lift.project.entity.ProjectRole;
import com.kulift.lift.project.repository.ProjectMemberRepository;
import com.kulift.lift.project.repository.ProjectRepository;
import com.kulift.lift.user.entity.User;
import com.kulift.lift.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjectMemberService {

	private final ProjectMemberRepository projectMemberRepository;
	private final ProjectRepository projectRepository;
	private final UserRepository userRepository;

	@Transactional
	public void addMember(Project project, User user, ProjectRole role) {
		projectMemberRepository.save(ProjectMember.builder()
			.project(project)
			.user(user)
			.role(role)
			.build());
	}

	@Transactional(readOnly = true)
	public List<ProjectResponse> getUserProjects(Long userId) {
		List<ProjectMember> projectMembers = projectMemberRepository.findAllByUser_Id(userId);
		return projectMembers.stream()
			.map(m -> ProjectResponse.from(m.getProject()))
			.toList();
	}

	@Transactional
	public void inviteMember(String projectKey, String email, ProjectRole role) {
		Project project = findProjectByKey(projectKey);

		User user = userRepository.findByEmail(email)
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

		if (projectMemberRepository.findByProject_ProjectKeyAndUser_Id(projectKey, user.getId()).isPresent()) {
			throw new CustomException(ErrorCode.DUPLICATE_EMAIL);
		}

		addMember(project, user, role);
	}

	@Transactional(readOnly = true)
	public List<ProjectMemberResponse> getProjectMembers(String projectKey) {
		return projectMemberRepository.findAllByProject_ProjectKey(projectKey)
			.stream()
			.map(ProjectMemberResponse::from)
			.toList();
	}

	@Transactional
	public void removeMember(String projectKey, Long userId) {
		ProjectMember member = projectMemberRepository.findByProject_ProjectKeyAndUser_Id(projectKey, userId)
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

		if (member.getRole() == ProjectRole.OWNER) {
			throw new CustomException(ErrorCode.OWNER_CANNOT_BE_REMOVED);
		}

		projectMemberRepository.delete(member);
	}

	public void deleteAllByProjectKey(String projectKey) {
		projectMemberRepository.deleteAllByProject_ProjectKey(projectKey);
	}

	private Project findProjectByKey(String projectKey) {
		return projectRepository.findByProjectKey(projectKey)
			.orElseThrow(() -> new CustomException(ErrorCode.PROJECT_NOT_FOUND));
	}
}
