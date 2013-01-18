package org.wintrisstech.sudoku;

import java.util.Random;

/**
 * A Sudoku puzzle solver based on an iterative search.
 * 
 * @author Erik
 */
class Solver2 implements Runnable {

	// The puzzle that is being solved. A 0 represents an empty space
	private int puzzle[][];
	// A counter
	private int iterations = 0;
	// Set to true when done
	private boolean done = false;
	/**
	 * An array of all the spaces in the <code>puzzle</code>. After this array
	 * is initialized, each entry references a space in the <code>puzzle</code>.
	 * If <code>spaces[index]</code> references <code>puzzle[row][column]</code>
	 * , then <code>spaces[index] == 9 * row + column</code>. Vice versa, the
	 * row and column of the space that <code>spaces[index]</code> references
	 * are
	 * <ul>
	 * <li> <code>row == spaces[index] / 9</code>, and
	 * <li> <code>column == spaces[index] % 9</code>.
	 * </ul>
	 * Each space in the <code>puzzle</code> is referenced exactly once. Filled
	 * spaces precede empty spaces, but otherwise there is no particular order
	 * to entries in <code>spaces</code>.
	 */
	private int[] spaces = new int[9 * 9];
	/*
	 * An index into the spaces array.
	 */
	private int index = 0;
	// A random number generator used to shuffle the spaces array.
	private Random random = new Random();

	/**
	 * Solves a Sudoku puzzle.
	 * <p>
	 * The strategy is to prove that there is no solution by exhausting all
	 * possible solutions. It invokes the method proveImpossible(), which throws
	 * an exception if it, against all odds, discovers a solution.
	 * 
	 */
	@Override
	public void run() {
		long startTime = System.currentTimeMillis();
		initializeSpaces();
		if (findSolution()) {
			System.out.println("This puzzle has a solution.");
		} else {
			System.out.println("This puzzle has no solutions.");
		}
		System.out.format("Number of iterations: %d%n"
				+ "Time elapsed: %d ms%n", iterations,
				System.currentTimeMillis() - startTime);
		done = true;

	}

	private void initializeSpaces() {
		// initialize and shuffle the spaces array
		for (int k = 0; k < spaces.length; k++) {
			int randIndex = random.nextInt(k + 1);
			spaces[k] = spaces[randIndex];
			spaces[randIndex] = k;
		}
		// count the number of clues given and place all the spaces that contain
		// a clue at the front of the spaces array.
		int numClues = 0;
		for (int k = 0; k < spaces.length; k++) {
			if (puzzle[spaces[k] / 9][spaces[k] % 9] != 0) { // clue found
				// swap the place containing a clue to the front of the spaces
				// array
				int tmp = spaces[numClues];
				spaces[numClues] = spaces[k];
				spaces[k] = tmp;
				numClues++;
			}
		}
		index = numClues;
	}

	/**
	 * Finds the solution of a puzzle or determines that the puzzle has no
	 * solutions.
	 * <p>
	 * Pre-condition: There are no conflicts among the clues in
	 * <code>puzzle</code>.
	 * <p>
	 * Post-condition: If there are no solutions, then <code>puzzle</code> is in
	 * the same state as when this method was called. If there is at least one
	 * solution, then <code>puzzle</code> is filled with a solution.
	 * <p>
	 * The algorithm is based on a while-loop with the following invariants:
	 * <ol>
	 * <li>For all indices <code>k &lt index</code>, <code>spaces[k]</code>
	 * references a non-empty spaces in the puzzle. There are no conflicts among
	 * those non-empty spaces.
	 * <li>
	 * For all indices <code>k &gt index</code>, <code>spaces[k]</code>
	 * references an empty space in the puzzle.
	 * <li>
	 * <code>spaces[index]</code> references either an empty space or a space
	 * that must be given a higher value than the current value to find a
	 * solution.
	 * </ol>
	 * Calling <code>findEmptySpace()</code> has no effect on the correctness of
	 * the algorithm, but may have significant effect on the processing time.
	 * 
	 */
	private boolean findSolution() {

//		findEmptySpace();
		int numClues = index;
		/*
         */
		while (numClues <= index && index < spaces.length) {
			iterations++;
			int i = spaces[index] / 9;
			int j = spaces[index] % 9;
			boolean foundPossibleValue = false;
			for (int value = puzzle[i][j] + 1; value <= 9; value++) {
				if (possible(i, j, value)) {
					foundPossibleValue = true;
					puzzle[i][j] = value;
					index++;
//					findEmptySpace();
					break;
				}
			}
			if (!foundPossibleValue) { // backtrack
				puzzle[i][j] = 0;
				index--;
			}
		}
		return index >= spaces.length;
	}

	/**
	 * Searches for the empty space that has the least possibilities and puts it
	 * at the head of the remaining empty spaces in the spaces array.
	 */
	private void findEmptySpace() {
		// This implementation returns a space with least possibilities.
		int minIndex = -1;
		int minPossibilities = 10;
		for (int k = index; k < spaces.length; k++) {
			int i = spaces[k] / 9;
			int j = spaces[k] % 9;
			assert puzzle[i][j] == 0;
			int possibilities = 0;
			for (int value = 1; value <= 9; value++) {
				if (possible(i, j, value)) {
					possibilities++;
				}
			}
			if (possibilities < minPossibilities) {
				minIndex = k;
				minPossibilities = possibilities;
			}
		}
		if (minIndex > index) { // swap empty space to the front of empty
								// spaces.
			int p = spaces[index];
			spaces[index] = spaces[minIndex];
			spaces[minIndex] = p;
		}
	}

	/**
	 * Tests whether assigning number to puzzle[row][column] introduces the
	 * number twice in the same row, column or region.
	 * <p>
	 * Pre-cond: puzzle[row][column] == 0 and 1 &le; number &le; 9
	 * <p>
	 * Post-cond: No side effects
	 * 
	 * @param row
	 *            - row number between 0 and 8
	 * @param column
	 *            - column number between 0 and 8
	 * @param number
	 *            - a number between 1 and 9.
	 * @return true if, and only if, assigning puzzle[row][column] = number will
	 *         not introduce a duplicate.
	 */
	private boolean possible(int row, int column, int number) {
		// Check row and column:
		for (int k = 0; k < 9; k++) {
			if (puzzle[row][k] == number || puzzle[k][column] == number) {
				return false;
			}
		}
		// Check region:
		int i0 = row - row % 3; // first row index in region
		int j0 = column / 3 * 3; // first column index in region
		for (int i = i0; i < i0 + 3; i++) {
			for (int j = j0; j < j0 + 3; j++) {
				if (puzzle[i][j] == number) {
					return false;
				}
			}
		}
		// No duplicates found in row, column or region:
		return true;
	}

	/**
	 * Sets the puzzle that this Solver will solve. Note that the solver writes
	 * the solution, if any, onto (i.e., modifies) the puzzle argument.
	 * Therefore, apply defensive copying to the argument before passing it to
	 * this method.
	 * 
	 * @param puzzle
	 *            the puzzle to solve
	 */
	void setPuzzle(int[][] puzzle) {
		this.puzzle = puzzle;
		this.iterations = 0;
		this.done = false;
	}

	/**
	 * Test if the solver has completed.
	 * 
	 * @return true is the solver has either found a solution or proven that no
	 *         solution exists
	 */
	boolean isDone() {
		return done;
	}
}
