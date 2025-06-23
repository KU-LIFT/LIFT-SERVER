package com.kulift.lift.domain.ai.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class DocumentService {

	public String extractText(MultipartFile file) throws IOException {
		String name = file.getOriginalFilename();
		if (name != null && name.toLowerCase().endsWith(".docx")) {
			try (XWPFDocument doc = new XWPFDocument(file.getInputStream())) {
				return doc.getParagraphs().stream()
					.map(XWPFParagraph::getText)
					.collect(Collectors.joining("\n"));
			}
		}
		return new String(file.getBytes(), StandardCharsets.UTF_8);
	}
}
