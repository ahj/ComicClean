package com.ahj.comic;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.ahj.comic.filter.ComicFilter;
import com.ahj.comic.filter.ComicNameDeviationFilter;
import com.ahj.comic.io.Io;
import com.ahj.comic.io.IoRegistry;
import com.ahj.comic.util.FileType;
import com.ahj.comic.util.FileUtil;

public class Comic {
	public static void main(String[] args) {
		File file = new File(args[0]);
		File dir = file.getParentFile();
		File workDir = new File(dir, FileUtil.getBaseName(file));
		
		Comic test = new Comic();
		
		try {
			List<ComicImage> images = test.read(file, workDir);
			
			for (ComicImage image : images) {
                System.out.println(image.getName() + " (" + image.getWidth() + ", " + image.getHeight() + ") / " + image.getFile());
			}
			
			List<ComicImage> filteredImages = test.filterBy(images, new ComicNameDeviationFilter(0, true));  // zero-character deviation and ignore numbers
//			List<ComicImage> filteredImages = test.filterBy(images, new ComicSizeDeviationFilter(5));
//			List<ComicImage> filteredImages = test.filterBy(images, new ComicSizeDeviationFilter(5, 5, true));  // percentage
//			List<ComicImage> filteredImages = test.filterBy(images, new ComicSizeStandardDeviationFilter());
			
			File outputFile = new File(dir, FileUtil.getBaseName(file) + "_out.cbz");
			
			test.write(outputFile, FileType.CBZ, filteredImages);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Comic() {
		super();
	}
	
	public void write(File file, FileType type, List<ComicImage> images) throws IOException {
		Set<Io> ios = IoRegistry.INSTANCE.getModulesByFileType(type);
		
		if (ios.isEmpty()) {
			throw new IOException("Unsupported FileType: " + type);
		}
		
		Io io = ios.iterator().next();

		io.write(file, images);
	}
	
	public List<ComicImage> read(File file, File workDir) throws IOException {
		List<ComicImage> images = null;
		
		for (Io io : IoRegistry.INSTANCE.entries()) {
			try {
				images = io.read(file, workDir);
				
				// Successfully read the images
				break;
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		if (images == null) {
			throw new IOException("Failed to read the Comic Book file");
		}
		
		return images;
	}

	/**
	 * Performs a two-pass filter of the images.
	 * 
	 * @param images
	 * @param filter
	 * @return
	 */
	public List<ComicImage> filterBy(List<ComicImage> images, ComicFilter filter) {
		filter.analyze(images);
		
		List<ComicImage> filteredImages = new ArrayList<ComicImage>();
		
 		for (ComicImage image : images) {
 			if (filter.isComicImage(image)) {
 				filteredImages.add(image);
 			}
 		}
 		
		return filteredImages;
	}
}
