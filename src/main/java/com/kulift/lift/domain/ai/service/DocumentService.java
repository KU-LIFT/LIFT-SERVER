package com.kulift.lift.domain.ai.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class DocumentService {

	public String extractText(MultipartFile file) throws IOException {
		String name = file.getOriginalFilename();
		if (name == null) {
			throw new IllegalArgumentException("파일 이름이 없습니다.");
		}

		String lowerName = name.toLowerCase();

		if (lowerName.endsWith(".docx")) {
			return extractFromDocx(file);
		} else if (lowerName.endsWith(".pdf")) {
			return extractFromPdf(file);
		} else {
			// 일반 텍스트나 markdown 등 처리
			return new String(file.getBytes(), StandardCharsets.UTF_8);
		}
	}

	private String extractFromDocx(MultipartFile file) throws IOException {
		try (XWPFDocument doc = new XWPFDocument(file.getInputStream())) {
			return doc.getParagraphs().stream()
				.map(XWPFParagraph::getText)
				.collect(Collectors.joining("\n"));
		}
	}

	private String extractFromPdf(MultipartFile file) throws IOException {
		try (PDDocument document = PDDocument.load(file.getInputStream())) {
			PDFTextStripper stripper = new PDFTextStripper();
			return stripper.getText(document);
		}
	}
}
