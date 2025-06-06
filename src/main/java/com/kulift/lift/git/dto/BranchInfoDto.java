package com.kulift.lift.git.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class BranchInfoDto {

	@JsonProperty("name")
	private String name;

	@JsonProperty("commit")
	private CommitRef commit;

	public String getCommitSha() {
		return commit.getSha();
	}

	@Getter
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class CommitRef {
		@JsonProperty("sha")
		private String sha;
	}
}
