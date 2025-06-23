package com.kulift.lift.domain.project.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kulift.lift.domain.project.entity.ProjectMember;
import com.kulift.lift.domain.project.entity.ProjectRole;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long> {

	List<ProjectMember> findAllByUser_Id(Long userId);

	Optional<ProjectMember> findByProject_ProjectKeyAndUser_Id(String projectKey, Long userId);

	void deleteAllByProject_ProjectKey(String projectKey);

	Collection<ProjectMember> findAllByProject_ProjectKey(String projectKey);

	boolean existsByProject_ProjectKeyAndUser_IdAndRoleIn(String projectKey, Long userId, List<ProjectRole> roles);

	boolean existsByProject_ProjectKeyAndUser_Id(String projectKey, Long userId);

	boolean existsByProject_ProjectKeyAndUser_IdAndRole(String projectKey, Long userId, ProjectRole role);

	void deleteByUserId(Long userId);
}
