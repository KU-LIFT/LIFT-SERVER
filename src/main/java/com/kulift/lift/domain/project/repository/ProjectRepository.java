package com.kulift.lift.domain.project.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kulift.lift.domain.project.entity.Project;

public interface ProjectRepository extends JpaRepository<Project, Long> {
	Optional<Project> findByProjectKey(String projectKey);

	void deleteByProjectKey(String projectKey);

	boolean existsByProjectKey(String projectKey);
}
