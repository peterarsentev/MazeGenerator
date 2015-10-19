package com.odesk.maze;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * TODO: comment
 * @author parsentev
 * @since 19.10.2015
 */
public class FakeSolution implements MazeSolver {

	@Override
	public Iterable<Cell> solve(Cell[][] desk) {
		List<Cell> cells = new ArrayList<>();
		for (int i=0;i!=10;i++) {
			cells.add(desk[0][i]);
		}
		for (int i=0;i!=10;i++) {
			cells.add(desk[i][9]);
		}
		return cells;
	}
}
