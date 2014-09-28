package com.ahj.comic.app.action;

import com.ahj.comic.app.core.BaseAction;

public class FileOpenAction extends BaseAction {
	private static final long serialVersionUID = 1L;

	public static final String valueOf_ACTION_COMMAND_KEY = "FileOpen";
	
	public FileOpenAction() {
		putValue(NAME, "Open...");
		putValue(ACTION_COMMAND_KEY, valueOf_ACTION_COMMAND_KEY);
	}
}

