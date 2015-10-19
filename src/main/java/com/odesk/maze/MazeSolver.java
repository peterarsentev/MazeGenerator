package com.odesk.maze;


import java.util.Iterator;

/**
 * TODO: comment
 * @author parsentev
 * @since 19.10.2015
 */
public interface MazeSolver {
	Iterable<Cell> solve(final Cell[][] desk);
}
