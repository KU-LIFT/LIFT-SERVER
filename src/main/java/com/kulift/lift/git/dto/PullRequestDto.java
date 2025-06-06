package com.kulift.lift.git.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PullRequestDto {

	@JsonProperty("html_url")
	private String htmlUrl;

	private String state;

	@JsonProperty("head")
	private HeadInfo head;

	@Getter
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class HeadInfo {
		private String sha;
	}
}
