package com.ahj.comic;

import java.awt.Image;
import java.io.File;

public class ComicImage {
	private File file;
	private int width;
	private int height;
	private String name;
	private Image thumbnail;
	
	public ComicImage(String name, int width, int height, File file, Image thumbnail) {
		super();
		
		setName(name);
		setWidth(width);
		setHeight(height);
		setFile(file);
		setThumbnail(thumbnail);
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Image getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(Image thumbnail) {
		this.thumbnail = thumbnail;
	}
}
