package org.wintrisstech.sudoku;

import java.util.Arrays;

import junit.framework.TestCase;

public class SolverTest extends TestCase {

	private Solver solver = new Solver();

	public void testSolvingASinglePuzzle() throws Exception {
		int[][] puzzle = Puzzles.getPuzzle(0);
		solver.setPuzzle(puzzle);
		new Thread(solver).start();
		waitForSolution(1000);
		assertEquals(PuzzleSolutions.PUZZLE_1_SOLUTION, puzzle);
	}

	public void testSolvingAllPuzzles() throws Exception {
		for (int i = 0; i < Puzzles.ALL_PUZZLES.length; i++) {
			int[][] puzzle = Puzzles.getPuzzle(i);
			solver.setPuzzle(puzzle);
		        new Thread(solver).start();
			waitForSolution(1000);
			assertEquals(PuzzleSolutions.getSolution(i), puzzle);
		}
	}

	/**
	 * This method waits until either the solver is done or the timeout is
	 * reached
	 * 
	 * @param timeout
	 *            maximum miliseconds to wait
	 */
	private void waitForSolution(long timeout) {
		long start = System.currentTimeMillis();
		while (!solver.isDone() && System.currentTimeMillis() < start + timeout) {
			try {
				Thread.sleep(25);
			} catch (InterruptedException e) {
				break;
			}
		}
		assertTrue(solver.isDone());
	}

	/**
	 * AssertEquals extension for 2 dim int arrays - wish JUnit handled this
	 * natively!
	 */
	public static void assertEquals(int[][] expected, int[][] actual) {
		assertEquals(expected.length, actual.length);
		for (int i = 0; i < expected.length; i++) {
			String message = "row " + i + " did not match";
			assertEquals(message, Arrays.toString(expected[i]), Arrays.toString(actual[i]));
		}
	}
}
