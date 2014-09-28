package com.ahj.comic.filter;

import java.util.List;

import com.ahj.comic.ComicImage;

public interface ComicFilter {
	/**
	 * Called on the first pass of the filter to process
	 * any collected information.
	 * 
	 * @param images
	 */
	void analyze(List<ComicImage> images);

	/**
	 * Called on the final pass of filter to determine whether
	 * or not to include the image in the filter output.
	 * 
	 * @param image
	 * @return
	 */
	boolean isComicImage(ComicImage image);
}
