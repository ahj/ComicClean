package com.ahj.comic.app.core;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;


public class BaseAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	
	public static final String CONTROLLER = "Controller";
	
	public void actionPerformed(ActionEvent evt) {
		Controller controller = (Controller)getValue(CONTROLLER);
		if (controller != null) {
			controller.actionPerformed(evt);
		}
	}
	
	public BaseAction setController(Controller controller) {
		putValue(CONTROLLER, controller);
		return this;
	}
}
