package com.ahj.comic.io;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.ahj.comic.util.FileType;

public class IoRegistry {
    public final static IoRegistry INSTANCE = new IoRegistry();
	
	private Set<Io> entries = new HashSet<Io>();
		
    // Exists only to defeat instantiation.
    private IoRegistry() {
		entries.add(new PdfIo());
		entries.add(new CbrIo());
		entries.add(new CbzIo());
		entries.add(new CbtIo());
	}
	
	public Set<Io> getModulesByFileType(FileType fileType) {
		Set<Io> set = new HashSet<Io>();
		
		for (Io io : entries) {
			if (io.getFileTypes().contains(fileType)) {
				set.add(io);
			}
		}
		
		return set;
	}

	public Set<Io> entries() {
		return Collections.unmodifiableSet(entries);
	}
}
