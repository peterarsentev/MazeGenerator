package com.odesk.maze;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;

/**
 * TODO: comment
 * @author parsentev
 * @since 19.10.2015
 */
public class SovlerListener implements ActionListener {
	private final MazePanel mazePanel;

	public SovlerListener(MazePanel mazePanel) {
		this.mazePanel = mazePanel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		this.mazePanel.drawSolution();
	}
}
