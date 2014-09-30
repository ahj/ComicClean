package com.ahj.comic.app;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.ahj.comic.ComicBook;
import com.ahj.comic.ComicImage;
import com.ahj.comic.app.action.FileOpenAction;
import com.ahj.comic.app.action.FileSaveAsAction;
import com.ahj.comic.app.core.Application;
import com.ahj.comic.app.core.BaseAction;
import com.ahj.comic.app.core.ButtonGroupHelper;
import com.ahj.comic.util.ComicBookFormat;
import com.ahj.comic.util.FileUtil;

public class ComicApp extends JFrame implements Application {
	private static final long serialVersionUID = 1L;

	private ComicBook book = null;
	private JList<ComicImage> list;
	private JLabel view;
	
	public ComicApp() {
		super("Comic Converter");
		
		JMenu menuFile = new JMenu("File");
		menuFile.add(new FileOpenAction().setController(this));
		menuFile.add(new FileSaveAsAction().setController(this));
		
		JMenu menuFilter = new JMenu("Filter");
		ButtonGroup group = new ButtonGroup();
		group.add(menuFilter.add(new JRadioButtonMenuItem(new ToolFilterNoneAction())));
		group.add(menuFilter.add(new JRadioButtonMenuItem(new ToolFilterPixelDeviationAction())));
		group.add(menuFilter.add(new JRadioButtonMenuItem(new ToolFilterPixelStandardDeviationAction())));
		group.add(menuFilter.add(new JRadioButtonMenuItem(new ToolFilterNameFrequencyAction())));
		group.setSelected(ButtonGroupHelper.getModelWithActionCommand(group, ToolFilterNoneAction.valueFor_ACTION_COMMAND_KEY), true);
		
		JMenuBar mb = new JMenuBar();
		mb.add(menuFile);
		mb.add(menuFilter);
		
		setJMenuBar(mb);
	
		DefaultListModel<ComicImage> listModel = new DefaultListModel<ComicImage>();
		
		list = new JList<ComicImage>(listModel);
		list.setVisibleRowCount(1);
		list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		list.setCellRenderer(new ImageListCellRenderer());
		
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(BorderLayout.NORTH,
			new JScrollPane(list,
				JScrollPane.VERTICAL_SCROLLBAR_NEVER,  
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS
			)
		);
		view = new JLabel((String)null, JLabel.CENTER);
		getContentPane().add(BorderLayout.CENTER, view);
		
		list.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent evt) {
				if (evt.getValueIsAdjusting()) {
					return;
				}
				
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						ComicApp.this.updateView(list.getSelectedValue());
					}
				});
			}
		});
	}

	/**
	 * Updates the main view of the application with the image.
	 * TODO If two page mode is selected then show this image AND the next
	 * @param image
	 */
	public void updateView(ComicImage image) {
		JLabel view = this.view;
		
		try {
			Image fullImg = ImageIO.read(image.getFile());
			
			view.setIcon((image != null) ? new ImageIcon(fullImg): null);
		}
		catch (IOException e) {
			view.setIcon(null);
		}
	}
	
	public void fileOpen() {
	    JFileChooser chooser = new JFileChooser();
	    FileNameExtensionFilter filterCB = new FileNameExtensionFilter("Comic Book Formats", "cbz", "cbr");
	    FileNameExtensionFilter filterPDF = new FileNameExtensionFilter("PDF", "pdf");
	    chooser.addChoosableFileFilter(filterCB);
	    chooser.addChoosableFileFilter(filterPDF);
//	    chooser.setFileFilter(filter);
	    int returnVal = chooser.showOpenDialog(this);
	    if (returnVal == JFileChooser.APPROVE_OPTION) {
	    	File file = chooser.getSelectedFile();
	    	File workDir = new File(file.getParentFile(), FileUtil.getBaseName(file) + "_working");

	    	DefaultListModel<ComicImage> listModel = (DefaultListModel<ComicImage>)list.getModel();
    		listModel.clear();
	    	
    		try {
	    		book = ComicBook.read(file, workDir);
	    	
	    		for (ComicImage image : book.getImages()) {
	    			listModel.addElement(image);
	    		}
    		}
    		catch (IOException e) {
    			e.printStackTrace();
    		}
	    }
	}
	
	public void fileSaveAs() {
	    JFileChooser chooser = new JFileChooser();
	    chooser.setAcceptAllFileFilterUsed(false);
	    FileNameExtensionFilter filterCBZ = new FileNameExtensionFilter("CBZ", "cbz");
	    FileNameExtensionFilter filterCBT = new FileNameExtensionFilter("CBT", "cbt");
	    FileNameExtensionFilter filterPDF = new FileNameExtensionFilter("PDF", "pdf");
	    chooser.addChoosableFileFilter(filterCBZ);
	    chooser.addChoosableFileFilter(filterCBT);
	    chooser.addChoosableFileFilter(filterPDF);
	    chooser.setFileFilter(filterCBZ);
	    int returnVal = chooser.showSaveDialog(this);
	    if (returnVal == JFileChooser.APPROVE_OPTION) {
	    	File file = chooser.getSelectedFile();
	    	FileNameExtensionFilter filter = (FileNameExtensionFilter)chooser.getFileFilter();
	    	ComicBookFormat type = ComicBookFormat.extensionOf(filter.getExtensions()[0]);
	    	//List<ComicImage> list = listModelToList();

	    	try {
	    		book.write(file, type);
    		}
    		catch (IOException e) {
    			e.printStackTrace();
    		}
	    }		
	}
	
	private List<ComicImage> listModelToList() {
    	DefaultListModel<ComicImage> listModel = (DefaultListModel<ComicImage>)list.getModel();
    	List<ComicImage> list = new ArrayList<ComicImage>();
    	for (int i = 0; i < listModel.size(); i++) {
    		list.add(listModel.get(i));
    	}
    	return list;
	}
	
	public void toolFilter() {
//		model.filterBy(images, filter);
	}
	
	class ToolFilterPixelDeviationAction extends BaseAction {
		private static final long serialVersionUID = 1L;

		public ToolFilterPixelDeviationAction() {
			putValue(NAME, "Pixel Deviation By x Pixels");
		}
		
		@Override
		public void actionPerformed(ActionEvent evt) {
		}		
	}
	
	class ToolFilterPixelStandardDeviationAction extends AbstractAction {
		private static final long serialVersionUID = 1L;

		public ToolFilterPixelStandardDeviationAction() {
			putValue(NAME, "Pixel Standard Deviation");
		}
		
		@Override
		public void actionPerformed(ActionEvent evt) {
		}		
	}

	class ToolFilterNameFrequencyAction extends AbstractAction {
		private static final long serialVersionUID = 1L;

		public ToolFilterNameFrequencyAction() {
			putValue(NAME, "Name Frequency");
		}
		
		@Override
		public void actionPerformed(ActionEvent evt) {
		}		
	}
	
	class ToolFilterNoneAction extends AbstractAction {
		private static final long serialVersionUID = 1L;

		private static final String valueFor_ACTION_COMMAND_KEY = "FilterNone";
		
		public ToolFilterNoneAction() {
			putValue(NAME, "None");
			putValue(ACTION_COMMAND_KEY, valueFor_ACTION_COMMAND_KEY);
		}
		
		@Override
		public void actionPerformed(ActionEvent evt) {
		}		
	}

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		ComicApp main = new ComicApp();
		main.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		main.setSize(600, 500);
		main.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		if (evt.getActionCommand().equals(FileOpenAction.valueOf_ACTION_COMMAND_KEY)) {
			fileOpen();
		}
		else if (evt.getActionCommand().equals(FileSaveAsAction.valueOf_ACTION_COMMAND_KEY)) {
			fileSaveAs();
		}
	}
}
