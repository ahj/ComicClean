package com.ahj.comic;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.ahj.comic.filter.ComicFilter;
import com.ahj.comic.io.Io;
import com.ahj.comic.io.IoRegistry;
import com.ahj.comic.util.ComicBookFormat;

public class ComicBook {
	private List<ComicImage> images = null;
	
	public ComicBook() {
		this(new ArrayList<ComicImage>());
	}
	
	/**
	 * @deprecated  ComicBooks start off empty or get loaded using the read method.
	 * @param images
	 */
	public ComicBook(List<ComicImage> images) {
		super();
		
		this.images = images;
	}
	
	public List<ComicImage> getImages() {
		return Collections.unmodifiableList(images);
	}
	
	//TODO Metadata getter/setter
	
	
	public static final ComicBook read(File bookFile, File workDir) throws IOException {
		List<ComicImage> images = null;
		
		for (Io io : IoRegistry.INSTANCE.entries()) {
			try {
				images = io.read(bookFile, workDir);
				
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
		
		return new ComicBook(images);
	}
	public void write(File bookFile, ComicBookFormat type) throws IOException {
		Set<Io> ios = IoRegistry.INSTANCE.getModulesByFileType(type);
		
		if (ios.isEmpty()) {
			throw new IOException("Unsupported FileType: " + type);
		}
		
		// Assume the first one is the only / best match for output
		Io io = ios.iterator().next();

		io.write(bookFile, getImages());
	}
	
	/**
	 * Clears the current image filter
	 * 
	 * @return true if the filter was cleared, false if the filter was already empty
	 */
	public boolean clearFilter() {
		//TODO Implement
		return true;
	}

	/**
	 * Filters the images.
	 * This is not a dynamic filter.
	 * 
	 * TODO Change to maintain filter internally on the list of images (Glazed Lists)
	 * 
	 * @param filter
	 */
	public List<ComicImage> filterBy(ComicFilter filter) {
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
