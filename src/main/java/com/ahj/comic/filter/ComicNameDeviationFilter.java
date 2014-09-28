package com.ahj.comic.filter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ahj.comic.ComicImage;

public class ComicNameDeviationFilter implements ComicFilter {
	private int charDeviation;
	private boolean ignoreNumeric;
	private Map<String, Integer> counts = new HashMap<String, Integer>();
	private Map.Entry<String, Integer> maxEntry = null;
	
	public ComicNameDeviationFilter(int charDeviation, boolean ignoreNumeric) {
		super();
		
		this.charDeviation = charDeviation;
		this.ignoreNumeric = ignoreNumeric;
	}
	
	@Override
	public void analyze(List<ComicImage> images) {		
		// Create name to image mappings accounting for digits
		for (ComicImage image : images) {
			String name = adjustedName(image);
		
			counts.put(name, (counts.get(name) == null) ? 1 : counts.get(name) + 1);
		}
		
		for (Map.Entry<String, Integer> entry : counts.entrySet()) {
			if ((maxEntry == null) || (entry.getValue() > maxEntry.getValue())) {
				maxEntry = entry;
			}
		}
	}

	private String adjustedName(ComicImage image) {
		String name = ignoreNumeric ? image.getName().replaceAll("[0-9]","") : image.getName();
		return name;
	}
	
	@Override
	public boolean isComicImage(ComicImage image) {
		String name = adjustedName(image);

		//TODO implement charDeviation
		
		return maxEntry.getKey().equals(name);
	}
}
