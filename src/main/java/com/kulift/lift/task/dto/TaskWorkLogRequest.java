package com.kulift.lift.task.dto;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskWorkLogRequest {
    @Min(1)
    private int durationMinutes;

    private String description;
}
