package com.kulift.lift.board.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BoardTemplateResponse {

	private Long id;
	private String name;
	private List<String> templateColumns;

	public static BoardTemplateResponse from(com.kulift.lift.board.entity.BoardTemplate template) {
		return BoardTemplateResponse.builder()
			.id(template.getId())
			.name(template.getName())
			.templateColumns(template.getTemplateColumns())
			.build();
	}
}
