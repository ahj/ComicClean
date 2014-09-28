package com.ahj.comic.util;

public enum FileType {
	CBR("cbr"),
	CBZ("cbz"),
	CBT("cbt"),
	PDF("pdf");
	
	private String extension;
	
	private FileType(String extension) {
		this.extension = extension;
	}
	
	public String getExtension() {
		return extension;
	}
	
	public static FileType extensionOf(String extension) {
		for (FileType type : FileType.values()) {
			if (type.getExtension().equals(extension)) {
				return type;
			}
		}
		return null;
	}
}
