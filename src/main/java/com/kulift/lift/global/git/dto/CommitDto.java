package com.kulift.lift.global.git.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommitDto {

	private String sha;

	@JsonProperty("commit")
	private CommitInfo commitInfo;

	@JsonProperty("html_url")
	private String htmlUrl;

	@JsonProperty("author")
	private UserInfo authorInfo;

	@Getter
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class CommitInfo {
		@JsonProperty("message")
		private String message;

		@JsonProperty("author")
		private Author author;
	}

	@Getter
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Author {
		@JsonProperty("name")
		private String name;

		@JsonProperty("date")
		private String date;
	}

	@Getter
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class UserInfo {
		@JsonProperty("login")
		private String login;
	}
}
