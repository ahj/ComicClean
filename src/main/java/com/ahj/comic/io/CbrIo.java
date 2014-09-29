package com.ahj.comic.io;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ahj.comic.ComicImage;
import com.ahj.comic.util.ComicBookFormat;
import com.github.junrar.Archive;
import com.github.junrar.exception.RarException;
import com.github.junrar.rarfile.FileHeader;

/**
 * Cbr (rar) Comic Book File Format IO module.
 * Uses Apache Commons Compress TAR library.
 * https://github.com/edmund-wagner/junrar
 * 
 * @author hjones
 */
public class CbrIo extends AbstractIo {
	public static Logger logger = LoggerFactory.getLogger(CbrIo.class);
	
	public CbrIo() {
		super(ComicBookFormat.CBR);
	}

	@Override
	public List<ComicImage> read(File file, File workDir) throws IOException {
		List<ComicImage> images = new ArrayList<ComicImage>();
		
		Archive archive = null;
		
		try {
			archive = new Archive(file);
			
			if (archive.isEncrypted()) {
				logger.warn("Comic Book is encrypted cannot extract");
				throw new IOException("Encrypted comic book - cannot read!");
			}
			
		    FileHeader fh = null;
		    
		    while ((fh = archive.nextFileHeader()) != null) {
		    	if (fh.isEncrypted()) {
		    		logger.warn("file is encrypted cannot extract: " + fh.getFileNameString());
		    		continue;
		    	}
		    	
		    	logger.info("extracting: " + fh.getFileNameString());
		    	
	    		if (fh.isDirectory()) {
	    			continue;
	    		}
	    		
	    		String name = (fh.isFileHeader() && fh.isUnicode()) ? fh.getFileNameW() : fh.getFileNameString();
	    		File imageFile = new File(workDir, name);
	    		OutputStream stream = null;
	    		
		    	try {
		    		stream = new FileOutputStream(imageFile);
		    		archive.extractFile(fh, stream);
		    	}
		    	catch (IOException e) {
		    		logger.error("error extracting the file", e);
		    	}
		    	finally {
		    		if (stream != null) {
		    			stream.close();
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
		catch (NullPointerException e) {
			throw new IOException("Failed to load CBR file", e);
		}
		catch (RarException e) {
			throw new IOException("This don't work dumbass!");
		}
		finally {
			if (archive != null) {
				archive.close();
			}
		}
		
		return images;
	}
	
	@Override
	public void write(File file, List<ComicImage> images) throws IOException {
		throw new IOException("Writing is not supported");
	}
}
