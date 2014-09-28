package com.ahj.comic.util;

import java.io.File;

public class FileUtil {
	public static final String getExtension(File file) {
		int index = file.getName().lastIndexOf('.');
		return (index > 0) ? file.getName().substring(index + 1) : null;
	}
	
	public static final String getBaseName(File file) {
		int index = file.getName().lastIndexOf('.');
		return (index > 0) ? file.getName().substring(0, index) : file.getName();
		
	}
}
