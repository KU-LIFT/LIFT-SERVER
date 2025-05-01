package com.kulift.lift.project.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kulift.lift.global.exception.CustomException;
import com.kulift.lift.global.exception.ErrorCode;
import com.kulift.lift.project.entity.ProjectMember;
import com.kulift.lift.project.entity.ProjectRole;
import com.kulift.lift.project.repository.ProjectMemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjectRoleService {

	private final ProjectMemberRepository projectMemberRepository;

	@Transactional(readOnly = true)
	public boolean isMember(String projectKey, Long userId) {
		return projectMemberRepository.existsByProject_ProjectKeyAndUser_Id(projectKey, userId);
	}

	@Transactional(readOnly = true)
	public boolean isOwner(String projectKey, Long userId) {
		return projectMemberRepository.existsByProject_ProjectKeyAndUser_IdAndRole(projectKey, userId,
			ProjectRole.OWNER);
	}

	@Transactional(readOnly = true)
	public boolean isManagerOrHigher(String projectKey, Long userId) {
		return projectMemberRepository.existsByProject_ProjectKeyAndUser_IdAndRoleIn(projectKey, userId,
			List.of(ProjectRole.OWNER, ProjectRole.MANAGER));
	}

	@Transactional
	public void updateRole(String projectKey, Long userId, ProjectRole newRole) {
		ProjectMember member = projectMemberRepository.findByProject_ProjectKeyAndUser_Id(projectKey, userId)
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

		if (member.getRole() == ProjectRole.OWNER || newRole == ProjectRole.OWNER) {
			throw new CustomException(ErrorCode.OWNER_CANNOT_BE_CHANGED);
		}

		member.changeRole(newRole);
	}
}
