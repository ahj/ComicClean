package com.ahj.comic.io;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import com.ahj.comic.ComicImage;
import com.ahj.comic.util.FileType;

public interface Io {
	Set<FileType> getFileTypes();
	List<ComicImage> read(File file, File workDir) throws IOException;
	void write(File file, List<ComicImage> images) throws IOException;
}
