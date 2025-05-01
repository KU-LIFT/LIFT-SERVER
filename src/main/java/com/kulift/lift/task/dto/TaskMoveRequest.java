// TaskMoveRequest.java
package com.kulift.lift.task.dto;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskMoveRequest {
	private Long targetColumnId;
	@PositiveOrZero
	private int position;
}
