package com.ahj.comic.filter;

import java.util.List;

import com.ahj.comic.ComicImage;

public class ComicSizeDeviationFilter implements ComicFilter {
	private int widthDeviation;
	private int heightDeviation;
	private boolean percentage;
	private double widthCma = 0d;
	private double heightCma = 0d;
	private double widthLower;
	private double widthUpper;
	private double heightLower;
	private double heightUpper;
	
	public ComicSizeDeviationFilter(int deviation) {
		this(deviation, deviation, false);
	}
	
	public ComicSizeDeviationFilter(int widthDeviation, int heightDeviation) {
		this(widthDeviation, heightDeviation, false);
	}
	
	public ComicSizeDeviationFilter(int widthDeviation, int heightDeviation, boolean percentage) {
		super();
		
		this.widthDeviation = widthDeviation;
		this.heightDeviation = heightDeviation;
		this.percentage = percentage;
	}
	
	private double cma(int n1, double xn1, double cman) {
		return cman + ((xn1 - cman) / n1);
	}
	
	private double percent(double x, int percentage) {
		return (x * percentage) / 100d;
	}
	
	@Override
	public void analyze(List<ComicImage> images) {
		for (int n = 0; n < images.size(); n++) {
			ComicImage image = images.get(n);
			
			widthCma = cma(n + 1, image.getWidth(), widthCma);
			heightCma = cma(n + 1, image.getHeight(), heightCma);
		}
		
		System.out.println("Analysis");
		System.out.println("  width average = " + widthCma);
		System.out.println("  height average = " + heightCma);
		
		double widthDev  = percentage ? percent(widthCma,  widthDeviation) : widthDeviation;
		double heightDev = percentage ? percent(heightCma, widthDeviation) : heightDeviation;
		
		widthLower = widthCma - widthDev;
		widthUpper = widthCma + widthDev;
		heightLower = heightCma - heightDev;
		heightUpper = heightCma + heightDev;
	}
	
	@Override
	public boolean isComicImage(ComicImage image) {
		if ((image.getWidth() < widthLower) ||
		    (image.getWidth() > widthUpper)) {
			return false;
		}
		
		if ((image.getHeight() < heightLower) ||
			(image.getHeight() > heightUpper)) {
			return false;
		}
		
		return true;
	}
}
