package com.kulift.lift.board.dto;

import com.kulift.lift.board.entity.BoardColumn;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ColumnResponse {
	private Long id;
	private String name;

	public static ColumnResponse from(BoardColumn column) {
		return ColumnResponse.builder()
			.id(column.getId())
			.name(column.getName())
			.build();
	}
}
