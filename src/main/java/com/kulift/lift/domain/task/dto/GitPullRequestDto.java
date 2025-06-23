package com.kulift.lift.domain.task.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GitPullRequestDto {
	private String url;
	private String sha;
	private String state;
}
