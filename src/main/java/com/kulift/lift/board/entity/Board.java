package com.kulift.lift.board.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.kulift.lift.project.entity.Project;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderColumn;
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
public class Board {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 80)
	private String name;

	@Column(nullable = false)
	private String projectKey;    // 조회 최적화

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	private Project project;

	@Builder.Default
	@OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
	@OrderColumn(name = "order_idx")
	private List<BoardColumn> columns = new ArrayList<>();

	private LocalDateTime createdAt;

	public static Board create(String name, Project project) {
		return Board.builder()
			.name(name)
			.projectKey(project.getProjectKey())
			.project(project)
			.createdAt(LocalDateTime.now())
			.build();
	}
}
