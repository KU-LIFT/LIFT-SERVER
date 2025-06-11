package com.kulift.lift.board.dto;

import java.util.List;

import com.kulift.lift.board.entity.BoardColumn;
import com.kulift.lift.task.dto.TaskResponse;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ColumnResponse {
	private Long id;
	private String name;
	private Integer orderIdx;
	private List<TaskResponse> tasks;

	public static ColumnResponse from(BoardColumn column) {
		return ColumnResponse.builder()
			.id(column.getId())
			.name(column.getName())
			.orderIdx(column.getOrderIdx())
			.tasks(
				column.getTasks().stream()
					.map(TaskResponse::from)
					.toList()
			)
			.build();
	}
}
