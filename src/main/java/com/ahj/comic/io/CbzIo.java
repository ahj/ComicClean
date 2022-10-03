package com.ahj.comic.io;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;

import com.ahj.comic.ComicImage;
import com.ahj.comic.util.ComicBookFormat;

/**
 * Cbz (zip) Comic Book File Format IO module.
 * Uses Built-in (Core) Java Zip file support.
 * 
 * @author hjones
 */
public class CbzIo extends AbstractIo {
	public CbzIo() {
		super(ComicBookFormat.CBZ);
	}

	@Override
	public List<ComicImage> read(File file, File workDir) throws IOException {
		List<ComicImage> images = new ArrayList<ComicImage>();
		ZipEntry entry = null;
		ZipFile zipFile = null;
		
		try {
			zipFile = new ZipFile(file);
			
			Enumeration<? extends ZipEntry> en = zipFile.entries();
			
			for (; en.hasMoreElements(); entry = en.nextElement()) {
				InputStream eis = null;
				OutputStream fos = null;
				
				if (entry == null) {
					continue;
				}
				
				File imageFile = new File(workDir, entry.getName());

				if (!imageFile.toPath().normalize().startsWith(workDir.toPath().normalize())) {
					throw new IOException("Bad zip entry");
				}
				
 				if (entry.isDirectory() ||
 				    // Nasty hack to avoid outputing metadata folders that
 				    // automatically get created on OS X platform
 					(entry.getName().indexOf("__MACOSX") >= 0)) {
					continue;
				}
				
				// Standard un-zip...
				try {
					imageFile.getParentFile().mkdirs();
					
					eis = zipFile.getInputStream(entry);
					fos = new FileOutputStream(imageFile);
					
					IOUtils.copy(eis, fos);
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				finally {
					if (eis != null) {
						eis.close();
					}
					if (fos != null) {
						fos.close();
					}
				}

				// Now read the 'just expanded' zip artifact as an image
				try {
					BufferedImage image = ImageIO.read(imageFile);
					Image thumbnail = createThumbnail(image);
					ComicImage page = new ComicImage(entry.getName(), image.getWidth(), image.getHeight(), imageFile, thumbnail);
					
					images.add(page);
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		finally {
			if (zipFile != null) {
				zipFile.close();
			}
		}
		
		return images;
	}
	
	@Override
	public void write(File file, List<ComicImage> images) throws IOException {
		ZipOutputStream output = new ZipOutputStream(new FileOutputStream(file));

		for (ComicImage image : images) {
			insert(output, image);
		}
		
		output.close();
	}
	
	public void insert(ZipOutputStream output, ComicImage image) throws IOException {
		  
	      ZipEntry entry = new ZipEntry(image.getName());
	      output.putNextEntry(entry);

		  FileInputStream input = null;
		  
		  System.out.println("    - " + entry.getName());
		  
	      try {
			  input = new FileInputStream(image.getFile());
			  
			  IOUtils.copy(input, output);
	      }
	      finally {
	    	  if (input != null) {
	    		  input.close();
	    	  }
	      }
	      
	      output.closeEntry();
	}
}
