package org.wintrisstech.sudoku;

import java.util.Random;

/**
 * A Sudoku puzzle solver based on a recursive search
 * 
 * @author Erik
 */
class Solver implements Runnable {

	// An exception thrown when proving that a puzzle has no solution fails.
	@SuppressWarnings("serial")
	private static class SudokuException extends Exception {
	};

	// The puzzle that is being solved. A 0 represents an empty space
	private int puzzle[][];
	// A counter
	private int iterations = 0;
	// Set to true when done
	private boolean done = false;
	/*
	 * A one-dimensional array of all the index pairs of the spaces in the
	 * puzzle. The order in which these pairs occur is not significant, and the
	 * array may be shuffled to randomize the solution process. Each (row, col)
	 * pair is represented by an int which, when represented in hexadecimal, has
	 * two digits where the first digit is the row, and the second digit is the
	 * column.
	 */
	private int[] puzzleSpaces = new int[9 * 9];
	// The first empty space in the spaces array.
	private int firstEmpty;

	private Random random = new Random();

	/**
	 * Solves a SudokuGUI puzzle.
	 * <p>
	 * The strategy is to prove that there is no solution by exhausting all
	 * possible solutions. It invokes the method proveImpossible(), which throws
	 * an exception if it, against all odds, discovers a solution.
	 * 
	 */
	@Override
	public void run() {
		long startTime = System.currentTimeMillis();
		// Initialize the puzzleSpaces array:
		for (int i = 0, k = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++, k++) {
				assert k == 9 * i + j;
				puzzleSpaces[k] = i << 4 | j;
			}
		}
		firstEmpty = 0;
		while (!isEmpty(puzzleSpaces[firstEmpty])) {
			firstEmpty++;
		}
		// Move all the non-empty spaces to the beginning of puzzleSpaces
		// Invariant:
		// 1) For all i, if i < firstEmpty, then puzzleSpaces[i] is non-empty.
		// 2) For all i, if firstEmpty <= i < k, then puzzleSpaces[i] is empty.
		for (int k = firstEmpty + 1; k < puzzleSpaces.length; k++) {
			if (!isEmpty(puzzleSpaces[k])) {
				int tmp = puzzleSpaces[firstEmpty];
				puzzleSpaces[firstEmpty] = puzzleSpaces[k];
				puzzleSpaces[k] = tmp;
				firstEmpty++;
			}
		}
		// Shuffle the empty spaces
		for (int k = firstEmpty; k < puzzleSpaces.length; k++) {
			int randomIndex = random.nextInt(puzzleSpaces.length - k) + k;
			int tmp = puzzleSpaces[k];
			puzzleSpaces[k] = puzzleSpaces[randomIndex];
			puzzleSpaces[randomIndex] = tmp;
		}
		// Get down to work...
		try {
			proveImpossible();
			System.out.println("This puzzle has no solutions.");
		} catch (SudokuException ex) {
			System.out.println("This puzzle has a solution:");
			System.out.println("Number of iterations = " + iterations);
		} finally {
			done = true;
			System.out.println("Time used = "
					+ (System.currentTimeMillis() - startTime) + " ms.");
		}

	}

	/**
	 * Determines if a space is empty.
	 * 
	 * @param space
	 *            an index into the puzzleSpaces array.
	 * @return true if the space is empty, i.e., the value is 0.
	 */
	private boolean isEmpty(int space) {
		return puzzle[space >> 4][space & 0xf] == 0;
	}

	/**
	 * Proves that there is no solution for the puzzle.
	 * <p>
	 * Pre-condition: No number occurs more than once in each row, column, and
	 * 3x3 square (a.k.a. region).
	 * <p>
	 * Post-condition: If this method exits normally (i.e., without throwing an
	 * exception), the puzzle with the assignment of values when this method is
	 * called is impossible to solve. The variable puzzle is in the same state
	 * as prior to the invocation of this method.
	 * <p>
	 * 
	 * @throws SudokuException
	 *             if it fails to prove that there is no solution. In this case,
	 *             the variable puzzle contains the solution.
	 */
	private void proveImpossible() throws SudokuException {
		int space = findEmptySpace(); // Throws an exception if no empty space
										// is found.
		firstEmpty++;
		int row = space >> 4;
		int col = space & 0xf;
		for (int value = 1; value < 10; value++) {
			if (possible(row, col, value)) {
				puzzle[row][col] = value;
				// For animation purposes, add some sleep time here.
				iterations++;
				proveImpossible();
			}
		}
		// Following code lines are to ensure that the puzzle is in the same
		// state as prior to the invocation of this method.
		firstEmpty--;
		puzzle[row][col] = 0;
	}

	/**
	 * Returns an empty space in the puzzle, if any.
	 * <p>
	 * Post-cond: Variable puzzle in same state as prior to call.
	 * 
	 * @return an int representing an empty space in the puzzle
	 * @throws SudokuException
	 *             if puzzle has no empty spaces left.
	 */
	/*
	private int findEmptySpace() throws SudokuException {
		// Simple (silly) implementation, which returns the first empty space.
		if (firstEmpty < puzzleSpaces.length) {
			return puzzleSpaces[firstEmpty];
		}
		throw new SudokuException();
	}
	*/
	private int findEmptySpace() throws SudokuException {
		// This implementation returns a space with least possibilities.
		int minPossibilities = 10;
		// Find a space with minimum number of possibilities
		int index = -1; // index of the space to return
		int row, col;
		for (int k = firstEmpty; k < puzzleSpaces.length; k++) {
			row = puzzleSpaces[k] >> 4;
			col = puzzleSpaces[k] & 0xf;
			// count the possibilities
			int possibilities = 0;
			for (int n = 1; n < 10; n++) {
				if (possible(row, col, n)) {
					possibilities++;
				}
			}
			if (possibilities < minPossibilities) {
				minPossibilities = possibilities;
				index = k;
			}
		}
		if (index == -1) { // No empty space found
			throw new SudokuException();
		}
		// Move the space to return to the first position among empty spaces.
		int tmp = puzzleSpaces[firstEmpty];
		puzzleSpaces[firstEmpty] = puzzleSpaces[index];
		puzzleSpaces[index] = tmp;
		return puzzleSpaces[firstEmpty]; // return the first empty position
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
