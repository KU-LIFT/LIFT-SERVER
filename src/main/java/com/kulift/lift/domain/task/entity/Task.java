package com.kulift.lift.domain.task.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.kulift.lift.domain.auth.entity.User;
import com.kulift.lift.domain.board.entity.BoardColumn;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
public class Task {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 100)
	private String name;

	@Column(length = 1000)
	private String description;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "column_id")
	private BoardColumn column;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "assignee_id")
	private User assignee;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private Priority priority;

	private LocalDateTime dueDate;

	@ElementCollection
	@CollectionTable(name = "task_tags", joinColumns = @JoinColumn(name = "task_id"))
	@Column(name = "tag", length = 50)
	private List<String> tags;

	@Column(nullable = false, updatable = false)
	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "created_by")
	private User createdBy;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "updated_by")
	private User updatedBy;

	@Column(length = 100)
	private String githubBranch;

	@Column(length = 200)
	private String githubPullRequestUrl;

	@Column(length = 100)
	private String githubLastCommitSha;

	@OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<TaskFile> files;

	public void linkGitInfo(String branch, String prUrl, String commitSha) {
		this.githubBranch = branch;
		this.githubPullRequestUrl = prUrl;
		this.githubLastCommitSha = commitSha;
	}
}
