package com.kulift.lift.project.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Project {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private String projectKey;

	@Column(nullable = false)
	private String name;

	private String description;

	private LocalDateTime createdAt;

	private Long githubInstallationId;

	private String githubRepoOwner;

	private String githubRepoName;

	private String githubDefaultBranch;

	public void update(String name, String description) {
		this.name = name;
		this.description = description;
	}

	public void connectGithubRepository(Long installationId, String owner, String name, String defaultBranch) {
		this.githubInstallationId = installationId;
		this.githubRepoOwner = owner;
		this.githubRepoName = name;
		this.githubDefaultBranch = defaultBranch;
	}
}
