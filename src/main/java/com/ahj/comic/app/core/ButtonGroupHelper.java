package com.ahj.comic.app.core;

import java.util.Enumeration;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;

public class ButtonGroupHelper {
	public static ButtonModel getModelWithActionCommand(ButtonGroup group, String actionCommand) {
		Enumeration<AbstractButton> en = group.getElements();
		while (en.hasMoreElements()) {
			AbstractButton btn = en.nextElement();
			if (btn.getActionCommand().equals(actionCommand)) {
				return btn.getModel();
			}
		}
		return null;
	}
}
