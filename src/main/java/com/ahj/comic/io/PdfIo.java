package com.ahj.comic.io;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;

import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDCcitt;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDJpeg;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDPixelMap;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDXObjectImage;

import com.ahj.comic.ComicImage;
import com.ahj.comic.util.FileType;

/**
 * Pdf Comic Book File Format IO module.
 * Uses Apache PDFBox library.
 * https://pdfbox.apache.org/
 * 
 * @author hjones
 */
public class PdfIo extends AbstractIo {

	public PdfIo() {
		super(FileType.PDF);
	}
	
	@Override
	public List<ComicImage> read(File file, File workDir) throws IOException {
		PDDocument doc = null;
		List<ComicImage> images = new ArrayList<ComicImage>();
		
		try {
		   doc = PDDocument.load(file);
		   
		   for (Object page : doc.getDocumentCatalog().getAllPages()) {
			   images.addAll(processPage((PDPage)page, workDir));
		   }
		   
		}
		finally {
		   if (doc != null) {
		       doc.close();
		   }
		}
		
		return images;
	}

	@Override
	public void write(File file, List<ComicImage> images) throws IOException {
		PDDocument document = new PDDocument();

		for (ComicImage image : images) {
			//Make each page exactly the same size as the image
			PDPage page = new PDPage(new PDRectangle(image.getWidth(), image.getHeight()));
			document.addPage(page);
			FileInputStream fis = null;
			RandomAccessFile raf = null;
			
	        PDXObjectImage ximage = null;
	        if (image.getName().toLowerCase().endsWith(".jpg")) {
	            ximage = new PDJpeg(document, fis = new FileInputStream(image.getFile()));
	        }
	        else if (image.getName().toLowerCase().endsWith(".tif") || image.getName().toLowerCase().endsWith(".tiff")) {
	            ximage = new PDCcitt(document, raf = new RandomAccessFile(image.getFile(), "r"));
	        }
	        else {
	            BufferedImage awtImage = ImageIO.read(image.getFile());
	            ximage = new PDPixelMap(document, awtImage);
	        }
	        
	        PDPageContentStream contentStream = new PDPageContentStream(document, page, true, true);
	        contentStream.drawImage(ximage, 0, 0);
	        contentStream.close();
	        if (fis != null) {
	        	fis.close();
	        }
	        if (raf != null) {
	        	raf.close();
	        }
		}
		
		try {
			document.save(file);
		} catch (COSVisitorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// finally make sure that the document is properly
		// closed.
		document.close();		
	}
	
	private Set<ComicImage> processPage(PDPage page, File workDir) throws IOException {
        Set<ComicImage> images = new HashSet<ComicImage>();
        PDResources resources = page.getResources();
        Map<String,PDXObject> objects = resources.getXObjects();
        
        for (Map.Entry<String, PDXObject> entry : objects.entrySet()) {
        	if (entry.getValue() instanceof PDXObjectImage) {
                PDXObjectImage image = (PDXObjectImage)entry.getValue();
                String name = entry.getKey() + "." + image.getSuffix();
				File file = new File(workDir, name);

               	file.getParentFile().mkdirs();
                
               	image.write2file(file);
               	
               	Image thumbnail = createThumbnail(file);
               	
               	images.add(new ComicImage(name, image.getWidth(), image.getHeight(), file, thumbnail));
        	}
        }
        
        return images;
	}
}
