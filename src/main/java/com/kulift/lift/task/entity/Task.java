package com.kulift.lift.task.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.kulift.lift.board.entity.BoardColumn;
import com.kulift.lift.user.entity.User;

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

	public static Task create(String name, String description, BoardColumn column, User assignee, Priority priority,
		LocalDateTime dueDate, List<String> tags, User creator) {
		return Task.builder()
			.name(name)
			.description(description)
			.column(column)
			.assignee(assignee)
			.priority(priority)
			.dueDate(dueDate)
			.tags(tags)
			.createdBy(creator)
			.createdAt(LocalDateTime.now())
			.build();
	}

	public void update(String name, String description, User assignee, Priority priority, LocalDateTime dueDate,
		List<String> tags, User updater) {
		this.name = name;
		this.description = description;
		this.assignee = assignee;
		this.priority = priority;
		this.dueDate = dueDate;
		this.tags = tags;
		this.updatedBy = updater;
		this.updatedAt = LocalDateTime.now();
	}

}
