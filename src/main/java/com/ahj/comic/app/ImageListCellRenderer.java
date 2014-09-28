package com.ahj.comic.app;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;

import com.ahj.comic.ComicImage;

public class ImageListCellRenderer extends DefaultListCellRenderer {
	private static final long serialVersionUID = 1L;

	public ImageListCellRenderer() {
		super();
		setHorizontalAlignment(JLabel.CENTER);
		setVerticalTextPosition(JLabel.BOTTOM);
		setHorizontalTextPosition(JLabel.CENTER);
	}
	
	@Override
	public Component getListCellRendererComponent(JList<?> list, 
                                                Object value, 
                                                int cellIndex, 
                                                boolean isSelected, 
                                                boolean cellHasFocus) {
		ComicImage image = (ComicImage)value;
        super.getListCellRendererComponent(list, value, cellIndex, isSelected, cellHasFocus);
		setText(image.getName());
		setIcon(new ImageIcon(image.getThumbnail()));
        return this;
    }
}