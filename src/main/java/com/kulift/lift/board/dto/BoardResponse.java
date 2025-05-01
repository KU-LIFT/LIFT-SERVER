package com.kulift.lift.board.dto;

import java.util.List;

import com.kulift.lift.board.entity.Board;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BoardResponse {
	private Long id;
	private String name;
	private List<ColumnResponse> columns;

	public static BoardResponse from(Board board) {
		return BoardResponse.builder()
			.id(board.getId())
			.name(board.getName())
			.columns(
				board.getColumns().stream()
					.map(ColumnResponse::from)
					.toList()
			)
			.build();
	}
}
