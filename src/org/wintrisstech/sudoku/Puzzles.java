package org.wintrisstech.sudoku;

// WARN: Don't control-shift-f this file, otherwise it will squish together =(
public abstract class Puzzles {

	/**
	 * This puzzle is considered to be a near worst case for brute force
	 * algorithms.
	 */
	public final static int[][] PUZZLE_0 = new int[][] {
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0 }, //
			{ 0, 0, 0, 0, 0, 3, 0, 8, 5 }, //
			{ 0, 0, 1, 0, 2, 0, 0, 0, 0 }, //
			{ 0, 0, 0, 5, 0, 7, 0, 0, 0 }, //
			{ 0, 0, 4, 0, 0, 0, 1, 0, 0 }, //
			{ 0, 9, 0, 0, 0, 0, 0, 0, 0 }, //
			{ 5, 0, 0, 0, 0, 0, 0, 7, 3 }, //
			{ 0, 0, 2, 0, 1, 0, 0, 0, 0 }, //
			{ 0, 0, 0, 0, 4, 0, 0, 0, 9 } };

	/**
	 * This is the Sudoku puzzle in San Diego Union Tribune, April 25, 2009:
	 */
	public final static int[][] PUZZLE_1 = new int[][] {
			{ 5, 0, 0, 0, 3, 0, 0, 9, 0 }, //
			{ 0, 2, 1, 0, 0, 9, 0, 0, 0 }, //
			{ 0, 6, 0, 0, 0, 0, 0, 1, 0 }, //
			{ 0, 0, 5, 0, 9, 8, 3, 6, 0 }, //
			{ 6, 0, 0, 0, 1, 0, 0, 0, 8 }, //
			{ 0, 1, 8, 3, 2, 0, 5, 0, 0 }, //
			{ 0, 9, 0, 0, 0, 0, 0, 8, 0 }, //
			{ 0, 0, 0, 4, 0, 0, 9, 2, 0 }, //
			{ 0, 5, 0, 0, 7, 0, 0, 0, 4 } };

	/**
	 * This is the Sudoku puzzle in San Diego Union Tribune, May 2, 2009:
	 */
	public final static int[][] PUZZLE_2 = new int[][] {
			{ 0, 4, 0, 6, 0, 0, 0, 0, 7 }, //
			{ 1, 3, 8, 9, 0, 0, 5, 0, 0 }, //
			{ 7, 0, 0, 0, 0, 0, 0, 0, 0 }, //
			{ 0, 0, 1, 5, 7, 0, 0, 3, 0 }, //
			{ 3, 0, 0, 0, 0, 0, 0, 0, 2 }, //
			{ 0, 5, 0, 0, 1, 9, 7, 0, 0 }, //
			{ 0, 0, 0, 0, 0, 0, 0, 0, 5 }, //
			{ 0, 0, 7, 0, 0, 3, 6, 2, 4 }, //
			{ 4, 0, 0, 0, 0, 8, 0, 7, 0 } };

	/**
	 * This is the Sudoku puzzle in San Diego Union Tribune, May 2, 2009:
	 */
	public final static int[][] PUZZLE_3 = new int[][] {
			{ 0, 0, 0, 0, 0, 0, 0, 0, 5 }, //
			{ 0, 0, 0, 0, 0, 0, 0, 8, 0 }, //
			{ 6, 0, 5, 3, 1, 4, 7, 0, 0 }, //
			{ 0, 0, 0, 6, 0, 0, 0, 3, 4 }, //
			{ 4, 2, 0, 0, 9, 0, 0, 0, 0 }, //
			{ 0, 0, 7, 5, 4, 0, 1, 0, 0 }, //
			{ 0, 0, 8, 7, 5, 2, 6, 0, 9 }, //
			{ 0, 9, 0, 0, 0, 0, 0, 0, 0 }, //
			{ 1, 0, 0, 0, 0, 0, 0, 0, 0 } };
	/**
	 * This is the same Sudoku puzzle as PUZZLE_3, except that the first empty
	 * space has been filled so it has no solutions:
	 */
	public final static int[][] PUZZLE_4 = new int[][] {
			{ 1, 0, 0, 0, 0, 0, 0, 0, 5 }, //
			{ 0, 0, 0, 0, 0, 0, 0, 8, 0 }, //
			{ 6, 0, 5, 3, 1, 4, 7, 0, 0 }, //
			{ 0, 0, 0, 6, 0, 0, 0, 3, 4 }, //
			{ 4, 2, 0, 0, 9, 0, 0, 0, 0 }, //
			{ 0, 0, 7, 5, 4, 0, 1, 0, 0 }, //
			{ 0, 0, 8, 7, 5, 2, 6, 0, 9 }, //
			{ 0, 9, 0, 0, 0, 0, 0, 0, 0 }, //
			{ 1, 0, 0, 0, 0, 0, 0, 0, 0 } };

	public static int[][][] ALL_PUZZLES = { PUZZLE_1, PUZZLE_2, PUZZLE_3,
			// PUZZLE_4,
			PUZZLE_0 };

	/**
	 * Gets a copy of one of the static puzzles defined in this class.
	 * 
	 * @param puzzleNumber
	 *            an integer that identifies the puzzle. Must be between 0 and
	 *            allPuzzles.length;
	 * @return a puzzle
	 */
	public static int[][] getPuzzle(int puzzleNumber) {
		return getPuzzle(puzzleNumber, ALL_PUZZLES);
	}

	/**
	 * Gets a copy of a puzzle from an array of puzzles. Defensive copying
	 * allows the caller to what it wishes with the returned puzzle without
	 * affecting the puzzles in the array.
	 * 
	 * @param puzzleNumber
	 *            the index of the puzzle
	 * @param puzzles
	 *            the array of puzzles
	 * @return the copy of the puzzle
	 * @throws IndexOutOfBoundsException
	 *             if the index is out of bounds
	 */
	public static int[][] getPuzzle(int puzzleNumber, int[][][] puzzles)
			throws IndexOutOfBoundsException {
		assert 0 <= puzzleNumber && puzzleNumber < puzzles.length;
		int[][] puzzle = puzzles[puzzleNumber].clone(); // shallow clone
		for (int i = 0; i < puzzle.length; i++) { // deep clone
			puzzle[i] = puzzle[i].clone();
		}
		return puzzle;
	}
}
