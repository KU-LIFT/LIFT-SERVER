package com.kulift.lift.domain.task.dto;

import java.time.LocalDateTime;

import lombok.Getter;

@Getter
public class TaskAttachmentDto {
	private Long id;
	private String filename;
	private String url;
	private Long uploadedById;
	private LocalDateTime uploadedAt;
}
