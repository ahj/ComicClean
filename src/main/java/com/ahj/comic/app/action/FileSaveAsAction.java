package com.ahj.comic.app.action;

import com.ahj.comic.app.core.BaseAction;

public class FileSaveAsAction extends BaseAction {
	private static final long serialVersionUID = 1L;

	public static final String valueOf_ACTION_COMMAND_KEY = "FileSaveAs";
	
	public FileSaveAsAction() {
		putValue(NAME, "Save As...");
		putValue(ACTION_COMMAND_KEY, valueOf_ACTION_COMMAND_KEY);
	}
}

