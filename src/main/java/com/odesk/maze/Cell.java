package com.odesk.maze;

import java.util.ArrayList;

/**
 * TODO: comment
 * @author parsentev
 * @since 19.10.2015
 */
public class Cell {
	public volatile Cell root;
	public ArrayList<Cell> children = new ArrayList<Cell>();
	public int xDesk;
	public int yDesk;
	public int x;
	public int y;
	public int size;
	public boolean topBottom = false;
	public boolean leftRight = false;
	public boolean topLeft = false;
	public boolean topRight = false;
	public boolean bottomLeft = false;
	public boolean bottomRight = false;
	public boolean topCenter = false;
	public boolean rightCenter = false;
	public boolean leftCenter = false;
	public boolean bottomCenter = false;
	public boolean visited = false;
	public boolean top = false;
	public boolean left = false;
	public boolean right = false;
	public boolean bottom = false;
	public boolean start;
	public boolean finish;

	public Cell(int x, int y, int size) {
		this.x = x;
		this.y = y;
		this.size = size;
	}

	public void cleanup() {
		this.topBottom = false;
		this.leftRight = false;
		this.topLeft = false;
		this.topRight = false;
		this.bottomLeft = false;
		this.bottomRight = false;
		this.topCenter = false;
		this.rightCenter = false;
		this.leftCenter = false;
		this.bottomCenter = false;
		this.visited = false;
		this.top = false;
		this.left = false;
		this.right = false;
		this.bottom = false;
		this.root = null;
		this.children.clear();
	}
}
