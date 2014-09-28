package com.ahj.comic.io;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.pdfbox.io.IOUtils;

import com.ahj.comic.ComicImage;
import com.ahj.comic.util.FileType;

/**
 * Cbt (tar) Comic Book File Format IO module.
 * Uses Apache Commons Compress TAR library.
 * http://commons.apache.org/proper/commons-compress/
 * 
 * @author hjones
 */
public class CbtIo extends AbstractIo {
	public CbtIo() {
		super(FileType.CBT);
	}

	@Override
	public List<ComicImage> read(File file, File workDir) throws IOException {
		List<ComicImage> images = new ArrayList<ComicImage>();
		
	    TarArchiveInputStream tarIn = null;

	    try {
	    	tarIn = new TarArchiveInputStream(new GzipCompressorInputStream(
	                    new BufferedInputStream(new FileInputStream(file))));
	    	
	    	ArchiveEntry entry = null;
	    	
	        while ((entry = tarIn.getNextTarEntry()) != null) {// create a file with the same name as the tarEntry
	            String name = entry.getName();
	        	File imageFile = new File(workDir, name);
	            
	            System.out.println("working: " + imageFile.getCanonicalPath());
	            
	            if (entry.isDirectory()) {
	            	imageFile.mkdirs();
	                continue;
	            }
	            
            	FileOutputStream fileOut = null;
            	
            	try {
            	    fileOut = new FileOutputStream(imageFile);
            	
                   	IOUtils.copy(tarIn, fileOut);
            	}
            	finally {
            		if (fileOut != null) {
            			fileOut.close();
            		}
            	}
            	
				// Now read the 'just expanded' zip artifact as an image
				try {
					BufferedImage image = ImageIO.read(imageFile);
					Image thumbnail = createThumbnail(imageFile);
	               	
					ComicImage page = new ComicImage(name, image.getWidth(), image.getHeight(), imageFile, thumbnail);
					
					images.add(page);
				}
				catch (Exception e) {
					e.printStackTrace();
				}
	        }
	    }
	    finally {
	    	if (tarIn != null) {
	    		tarIn.close();
	    	}
	    }
		return images;
	}
	
	@Override
	public void write(File file, List<ComicImage> images) throws IOException {
		TarArchiveOutputStream os = null;
		
		try {
			os = new TarArchiveOutputStream(new GzipCompressorOutputStream(
					new BufferedOutputStream(new FileOutputStream(file))));
			
			ArchiveEntry entry = null;
			
			for (ComicImage image : images) {
				entry = os.createArchiveEntry(image.getFile(), image.getName());

				os.putArchiveEntry(entry);

				FileInputStream fis = null;
				
				try {
					fis = new FileInputStream(image.getFile());
					
					IOUtils.copy(fis, os);
				}
				finally {
					if (fis != null) {
						fis.close();
					}
					
					os.closeArchiveEntry();
				}
			}
		}
		finally {
			if (os != null) {
				os.close();
			}
		}
	}
}
