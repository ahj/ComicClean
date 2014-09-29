package com.ahj.comic.test;

import java.io.File;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import com.ahj.comic.ComicBook;
import com.ahj.comic.util.FileUtil;

public class Load {
	@Test
	public void testLoadCbz() {
		File file = new File(Load.class.getResource("/test.cbz").getFile());
		File workFile = new File(file.getParentFile(), FileUtil.getBaseName(file) + "-work");
		
		try {
			ComicBook book = ComicBook.read(file,  workFile);
		
			Assert.assertEquals(4, book.getImages().size());
		}
		catch (IOException e) {
			Assert.fail("IOException occured during test");
			e.printStackTrace();
		}
	}
	
	@Test
	public void testLoadPdf() {
		File file = new File(Load.class.getResource("/test.pdf").getFile());
		File workFile = new File(file.getParentFile(), FileUtil.getBaseName(file) + "-work");
		
		try {
			ComicBook book = ComicBook.read(file, workFile);
		
			Assert.assertEquals(4, book.getImages().size());
		}
		catch (IOException e) {
			Assert.fail("IOException occured during test");
			e.printStackTrace();
		}
	}
}
