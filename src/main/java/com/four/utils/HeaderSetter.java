package com.four.utils;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

public class HeaderSetter {
	
	public static HttpHeaders setHeader(String fileName) {
		HttpHeaders header = new HttpHeaders();
		String fileType = fileName.substring(fileName.lastIndexOf(".") + 1);
		switch (fileType) {
			case "png":
				header.setContentType(MediaType.IMAGE_PNG);
				break;
			case "gif":
				header.setContentType(MediaType.IMAGE_GIF);
				break;
			case "jpg":
			case "jpeg":
				header.setContentType(MediaType.IMAGE_JPEG);
				break;
			case "webp":
				header.setContentType(MediaType.parseMediaType("image/webp"));
				break;
			default:
		}
		return header;
	}
}
