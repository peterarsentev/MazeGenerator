package com.odesk.maze;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * TODO: comment
 * @author parsentev
 * @since 19.10.2015
 */
public class ButtonAction extends JButton {

	public ButtonAction(String name, ActionListener listener) {
		this.setText(name);
		this.addActionListener(listener);
	}
}
