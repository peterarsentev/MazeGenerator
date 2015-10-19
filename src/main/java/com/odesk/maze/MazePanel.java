package com.odesk.maze;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Random;

public class MazePanel extends JPanel implements MouseListener {

	private final int totalCells = 10;
	private final int sizeDesk = 400;
	private final int step =  sizeDesk/totalCells;
	private final int padding = 10;
	private final int paddingCell = 5;
	private final Random random = new Random();
	private final MazeSolver solver;
	public final Cell[][] desk = new Cell[totalCells][totalCells];
	private Iterable<Cell> solution;

	public MazePanel(final MazeSolver solve) {
		this.solver = solve;
		this.addMouseListener(this);
		for (int x=0;x!=totalCells;x++) {
			for (int y = 0; y!=totalCells; y++) {
				Cell cell = new Cell(x*step, y*step, step);
				cell.xDesk = x;
				cell.yDesk = y;
				desk[x][y] = cell;
			}
		}
		this.desk[0][0].start = true;
		this.desk[totalCells-1][totalCells-1].finish = true;
	}

	public void cleanup() {
		this.solution = null;
		for (Cell[] row : desk) {
			for (Cell cell : row) {
				cell.cleanup();
			}
		}
	}

	public void generateMaze(Cell cell) throws InterruptedException {
		this.repaint();
		Thread.sleep(50);
		cell.visited = true;
		for (Integer pos : generateMove()) {
			if (pos == 0) {
				visitTop(cell);
			} else if (pos == 1) {
				visitRight(cell);
			} else if (pos == 2) {
				visitBottom(cell);
			} else if (pos == 3) {
				visitLeft(cell);
			}
		}
		drawExternal(cell);
	}

	private void drawExternal(Cell cell) {
		if (cell.root != null && cell.children.isEmpty()) {
			if (cell.root.top && cell.root.yDesk < cell.yDesk) {
				cell.topCenter = true;
			} else if (cell.root.left && cell.root.xDesk < cell.xDesk) {
				cell.leftCenter = true;
			} else if (cell.root.right && cell.root.xDesk > cell.xDesk) {
				cell.rightCenter = true;
			} else if (cell.root.bottom && cell.root.yDesk > cell.yDesk) {
				cell.bottomCenter = true;
			}
			this.repaint();
		}
	}

	private void drawRoot(Cell cell, boolean top, boolean right, boolean bottom, boolean left) {
		if (cell.root == null) {
			if (top) {
				cell.topCenter = true;
			} else if (right) {
				cell.rightCenter = true;
			} else if (bottom) {
				cell.bottomCenter = true;
			} else if (left) {
				cell.leftCenter = true;
			}
		}
	}

	public Integer[] generateMove() {
		ArrayList<Integer> list = new ArrayList<Integer>();
		while (list.size() < 4) {
			int rm = random.nextInt(4);
			if (!list.contains(rm)) {
				list.add(rm);
			}
		}
		return list.toArray(new Integer[list.size()]);
	}

	private boolean visitTop(Cell cell) throws InterruptedException {
		if (!visited(cell.xDesk, cell.yDesk - 1)) {
			fireCellTop(cell);
			Cell next = desk[cell.xDesk][cell.yDesk - 1];
			connectCell(cell, next);
			drawRoot(cell, true, false, false, false);
			generateMaze(next);
			return true;
		}
		return false;
	}

	private boolean visitRight(Cell cell) throws InterruptedException {
		if (!visited(cell.xDesk + 1, cell.yDesk)) {
			fireCellRight(cell);
			Cell next = desk[cell.xDesk + 1][cell.yDesk];
			connectCell(cell, next);
			drawRoot(cell, false, true, false, false);
			generateMaze(next);
			return true;
		}
		return false;
	}

	private boolean visitLeft(Cell cell) throws InterruptedException {
		if (!visited(cell.xDesk - 1, cell.yDesk)) {
			fireCellLeft(cell);
			Cell next = desk[cell.xDesk - 1][cell.yDesk];
			connectCell(cell, next);
			drawRoot(cell, false, false, false, true);
			generateMaze(next);
			return true;
		}
		return false;
	}

	private boolean visitBottom(Cell cell) throws InterruptedException {
		if (!visited(cell.xDesk, cell.yDesk + 1)) {
			fireCellBottom(cell);
			Cell next = desk[cell.xDesk][cell.yDesk + 1];
			connectCell(cell, next);
			drawRoot(cell, false, false, true, false);
			generateMaze(next);
			return true;
		}
		return false;
	}

	private void connectCell(Cell root, Cell child) {
		root.children.add(child);
		child.root = root;
	}

	private void fireCellTop(Cell cell) {
		if (cell.root != null) {
			if (cell.root.bottom && cell.root.yDesk > cell.yDesk ) {
				cell.topBottom = true;
			} else if (cell.root.right && cell.root.xDesk > cell.xDesk) {
				cell.topRight = true;
			} else if (cell.root.left && cell.root.xDesk < cell.xDesk) {
				cell.topLeft = true;
			}
		}
		cell.bottom = true;
	}

	private void fireCellRight(Cell cell) {
		if (cell.root != null) {
			if (cell.root.top && cell.root.yDesk < cell.yDesk) {
				cell.topRight = true;
			} else if (cell.root.bottom && cell.root.yDesk > cell.yDesk) {
				cell.bottomRight = true;
			} else if (cell.root.left && cell.root.xDesk < cell.xDesk) {
				cell.leftRight = true;
			}
		}
		cell.left = true;
	}

	private void fireCellBottom(Cell cell) {
		if (cell.root != null) {
			if (cell.root.top && cell.root.yDesk < cell.yDesk) {
				cell.topBottom = true;
			} else if (cell.root.right && cell.root.xDesk > cell.xDesk) {
				cell.bottomRight = true;
			} else if (cell.root.left && cell.root.xDesk < cell.xDesk) {
				cell.bottomLeft = true;
			}
		}
		cell.top = true;
	}

	private void fireCellLeft(Cell cell) {
		if (cell.root != null) {
			if (cell.root.top && cell.root.yDesk < cell.yDesk) {
				cell.topLeft = true;
			} else if (cell.root.right && cell.root.xDesk > cell.xDesk) {
				cell.leftRight = true;
			} else if (cell.root.bottom && cell.root.yDesk > cell.yDesk) {
				cell.bottomLeft = true;
			}
		}
		cell.right = true;
	}

	private boolean visited(int x, int y) {
		if (x >= 0 && x < desk.length && y >= 0 && y < desk.length) {
			return desk[x][y].visited;
		}
		return true;
	}

	@Override
	protected void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		for (Cell[] row : desk) {
			for (Cell cell : row) {
				graphics.setColor(Color.black);
				if (cell.visited) {
					graphics.setColor(Color.blue);
					drawElement(graphics, cell);
					if (cell.start) {
						graphics.setColor(Color.red);
						rectElement(graphics, cell, paddingCell);
					} else if (cell.finish) {
						graphics.setColor(Color.green);
						rectElement(graphics, cell, paddingCell);
					}
					graphics.setColor(Color.black);
				}
				graphics.drawRect(cell.x + padding, cell.y + padding, cell.size, cell.size);
			}
		}
		if (this.solution != null) {
			graphics.setColor(Color.red);
			for (Cell cell : this.solution) {
				rectElement(graphics, cell, 15);
			}
			graphics.setColor(Color.black);
		}
	}

	private void drawElement(Graphics graphics, Cell cell) {
		if (cell.topBottom) {
			topBottomElement(graphics, cell);
		}
		if (cell.topLeft) {
			topLeftElement(graphics, cell);
		}
		if (cell.topRight) {
			topRightElement(graphics, cell);
		}
		if (cell.leftRight) {
			leftRightElement(graphics, cell);
		}
		if (cell.bottomLeft) {
			bottomLeftElement(graphics, cell);
		}
		if (cell.bottomRight) {
			bottomRightElement(graphics, cell);
		}
		if (cell.topCenter) {
			topCenterElement(graphics, cell);
		}
		if (cell.bottomCenter) {
			bottomCenterElement(graphics, cell);
		}
		if (cell.leftCenter) {
			leftCenterElement(graphics, cell);
		}
		if (cell.rightCenter) {
			rightCenterElement(graphics, cell);
		}
	}

	private void rectElement(Graphics graphics, Cell cell, int pc) {
		graphics.fillRect(cell.x + padding + pc, cell.y + padding + pc, cell.size - 2 * pc, cell.size - 2 * pc);
	}


	private void topBottomElement(Graphics graphics, Cell cell) {
		graphics.fillRect(cell.x + padding + paddingCell, cell.y + padding, cell.size - 2 * paddingCell, cell.size);
	}

	private void leftRightElement(Graphics graphics, Cell cell) {
		graphics.fillRect(cell.x + padding, cell.y + padding + paddingCell, cell.size, cell.size - 2 * paddingCell);
	}

	private void topRightElement(Graphics graphics, Cell cell) {
		graphics.fillRect(cell.x + padding + paddingCell, cell.y + padding, cell.size - 2 * paddingCell, cell.size - 2 * paddingCell);
		graphics.fillRect(cell.x + padding + paddingCell, cell.y + padding + paddingCell, cell.size - paddingCell, cell.size - 2 * paddingCell);
	}

	private void topLeftElement(Graphics graphics, Cell cell) {
		graphics.fillRect(cell.x + padding + paddingCell, cell.y + padding, cell.size - 2 * paddingCell, cell.size - 2 * paddingCell);
		graphics.fillRect(cell.x + padding, cell.y + padding + paddingCell, cell.size - paddingCell, cell.size - 2 * paddingCell);
	}

	private void bottomRightElement(Graphics graphics, Cell cell) {
		graphics.fillRect(cell.x + padding + paddingCell, cell.y + padding + paddingCell, cell.size - 2 * paddingCell, cell.size - paddingCell);
		graphics.fillRect(cell.x + padding + paddingCell, cell.y + padding + paddingCell, cell.size - paddingCell, cell.size - 2 * paddingCell);
	}

	private void bottomLeftElement(Graphics graphics, Cell cell) {
		graphics.fillRect(cell.x + padding + paddingCell, cell.y + padding + paddingCell, cell.size - 2 * paddingCell, cell.size - paddingCell);
		graphics.fillRect(cell.x + padding, cell.y + padding + paddingCell, cell.size - paddingCell, cell.size - 2 * paddingCell);
	}

	private void topCenterElement(Graphics graphics, Cell cell) {
		graphics.fillRect(cell.x + padding + paddingCell, cell.y + padding, cell.size - 2 * paddingCell, cell.size - paddingCell);
	}

	private void leftCenterElement(Graphics graphics, Cell cell) {
		graphics.fillRect(cell.x + padding, cell.y + padding + paddingCell, cell.size - paddingCell, cell.size - 2 * paddingCell);
	}

	private void rightCenterElement(Graphics graphics, Cell cell) {
		graphics.fillRect(cell.x + padding + paddingCell, cell.y + padding + paddingCell, cell.size - paddingCell, cell.size - 2 * paddingCell);
	}

	private void bottomCenterElement(Graphics graphics, Cell cell) {
		graphics.fillRect(cell.x + padding + paddingCell, cell.y + padding + paddingCell, cell.size - 2 * paddingCell, cell.size - paddingCell);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int xClick = e.getX();
		int yClick = e.getY();
		for (Cell[] row : desk) {
			for (Cell cell : row) {
				boolean left = cell.x + padding < xClick;
				boolean right = cell.x + padding + step > xClick;
				boolean top = cell.y + padding < yClick;
				boolean bottom = cell.y + padding + step > yClick;
				if (left && right && top && bottom) {
					cell.visited = true;
					this.repaint();
					break;
				}
			}
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {

	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	public void drawSolution() {
		this.solution = this.solver.solve(this.desk);
		this.repaint();
	}
}
