package com.ahj.comic.util;

public enum ComicBookFormat {
	CBR("cbr"),
	CBZ("cbz"),
	CBT("cbt"),
	PDF("pdf");
	
	private String extension;
	
	private ComicBookFormat(String extension) {
		this.extension = extension;
	}
	
	public String getExtension() {
		return extension;
	}
	
	public static ComicBookFormat extensionOf(String extension) {
		for (ComicBookFormat type : ComicBookFormat.values()) {
			if (type.getExtension().equals(extension)) {
				return type;
			}
		}
		return null;
	}
}
