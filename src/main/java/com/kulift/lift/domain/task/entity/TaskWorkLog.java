package com.kulift.lift.domain.task.entity;

import java.time.LocalDateTime;

import com.kulift.lift.domain.auth.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class TaskWorkLog {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "task_id")
	private Task task;

	@Column(nullable = false)
	private int durationMinutes;

	@Column(length = 500)
	private String description;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "logged_by")
	private User loggedBy;

	@Column(nullable = false, updatable = false)
	private LocalDateTime loggedAt;

	private LocalDateTime updatedAt;

	@PrePersist
	void prePersist() {
		loggedAt = LocalDateTime.now();
	}

	@PreUpdate
	void preUpdate() {
		updatedAt = LocalDateTime.now();
	}

	public void updateLog(int newDuration, String newDescription) {
		this.durationMinutes = newDuration;
		this.description = newDescription;
		this.updatedAt = LocalDateTime.now();
	}
}
