package com.ahj.comic.io;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.imageio.ImageIO;

import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Method;

import com.ahj.comic.util.FileType;

public abstract class AbstractIo implements Io {
	private Set<FileType> fileTypes = new HashSet<FileType>();
	
	public AbstractIo(FileType ... fileTypes) {
		super();
		
		for (FileType fileType : fileTypes) {
			this.fileTypes.add(fileType);
		}
	}
	
	@Override
	public Set<FileType> getFileTypes() {
		return Collections.unmodifiableSet(fileTypes);
	}
	
	protected Image createThumbnail(File source) throws IOException {
       	return createThumbnail(ImageIO.read(source), 200, 200);
	}

	protected Image createThumbnail(BufferedImage source, int width, int height) {
		//return source.getScaledInstance(width, height, BufferedImage.SCALE_SMOOTH);
		
		return Scalr.resize(source, Method.QUALITY, width, height, Scalr.OP_ANTIALIAS);
	}
	
	
	
	private Set<String> usedFileNames = new HashSet<String>();
	
	protected String getUniqueFileName(String key, String suffix) {
		Integer index = null;
		String name = null;
		
		while ((index == null) || (index < 100)) {
			name = key + ((index == null) ? "" : index.toString()) + "." + suffix;
			
			if (!usedFileNames.contains(name)) {
				usedFileNames.add(name);
				break;
			}
			
			index = (index == null) ? 2 : index++;
		}
		
		return name;
	}
}
