package com.kulift.lift.board.dto;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ColumnOrderRequest {
	@NotEmpty
	private List<Long> orderedIds;
}
