package com.kulift.lift.board.entity;

import java.util.ArrayList;
import java.util.List;

import com.kulift.lift.task.entity.Task;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
public class BoardColumn {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 60)
	private String name;

	@Column(name = "order_idx")
	private Integer orderIdx;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	private Board board;

	@OneToMany(mappedBy = "column", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Task> tasks = new ArrayList<>();

	public static BoardColumn create(String name, Board board) {
		return BoardColumn.builder()
			.name(name)
			.board(board)
			.build();
	}

	public void addTask(Task task) {
		tasks.add(task);
		task.setColumn(this);
	}
}
