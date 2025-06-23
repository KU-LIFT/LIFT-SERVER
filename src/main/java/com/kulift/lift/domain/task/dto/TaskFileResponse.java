package com.kulift.lift.domain.task.dto;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.kulift.lift.domain.task.entity.TaskFile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskFileResponse {
	private Long id;
	private String fileName;
	private String fileType; // MIME 타입 (예: "image/png", "application/pdf")
	private String fileData;
	private String downloadUrl;

	public static TaskFileResponse of(TaskFile taskFile) {
		try {
			Path filePath = Path.of(taskFile.getFileUrl());
			String fileType = Files.probeContentType(filePath);

			TaskFileResponse dto = new TaskFileResponse();
			dto.setId(taskFile.getId());
			dto.setFileName(taskFile.getFileName());
			dto.setFileType(fileType);

			if (fileType != null && fileType.startsWith("image/")) {
				// 이미지 파일의 경우 Base64 인코딩
				byte[] fileBytes = Files.readAllBytes(filePath);
				dto.setFileData(Base64.getEncoder().encodeToString(fileBytes));
			} else {
				// 기타 파일은 다운로드 URL 제공
				dto.setDownloadUrl("/api/files/" + taskFile.getId());
			}

			return dto;
		} catch (Exception e) {
			throw new RuntimeException("파일 처리 중 오류 발생: " + e.getMessage(), e);
		}
	}

	public static List<TaskFileResponse> of(List<TaskFile> taskFiles) {
		return taskFiles != null
			? taskFiles.stream().map(TaskFileResponse::of).collect(Collectors.toList())
			: Collections.emptyList();
	}
}
