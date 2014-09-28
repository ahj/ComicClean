package com.ahj.comic.filter;

import java.util.List;

import com.ahj.comic.ComicImage;

public class ComicSizeStandardDeviationFilter implements ComicFilter {
	private double widthCma = 0d;
	private double heightCma = 0d;
	private double widthPsd = 0d;
	private double heightPsd = 0d;
	
	private double cma(int n1, double xn1, double cman) {
		return cman + ((xn1 - cman) / n1);
	}
	
	@Override
	public void analyze(List<ComicImage> images) {
		for (int n = 0; n < images.size(); n++) {
			ComicImage image = images.get(n);
			
			widthCma = cma(n + 1, image.getWidth(), widthCma);
			heightCma = cma(n + 1, image.getHeight(), heightCma);
		}
		
		double widthTotal = 0;
		double heightTotal = 0;
		for (ComicImage image : images) {
			double widthI = (image.getWidth() - widthCma);
			double widthI2 = widthI * widthI;
			widthTotal += widthI2;
			
			double heightI = (image.getHeight() - heightCma);
			double heightI2 = heightI * heightI;
			heightTotal += heightI2;
		}
		widthPsd = Math.sqrt(widthTotal / images.size());
		heightPsd = Math.sqrt(heightTotal / images.size());
		
		System.out.println("Analysis");
		System.out.println("  width average = " + widthCma);
		System.out.println("  width population standard deviation = " + widthPsd);
		System.out.println("  height average = " + heightCma);
		System.out.println("  height population standard deviation = " + heightPsd);
	}
	
	@Override
	public boolean isComicImage(ComicImage image) {
		if ((image.getWidth() < (widthCma - widthPsd)) ||
		    (image.getWidth() > (widthCma + widthPsd))) {
			return false;
		}
		if ((image.getHeight() < (heightCma - heightPsd)) ||
			(image.getHeight() > (heightCma + heightPsd))) {
				return false;
			}
		
		return true;
	}
}
