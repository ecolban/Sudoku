package org.wintrisstech.sudoku;

// WARN: Don't control-shift-f this file, otherwise it will squish together =(
public class PuzzleSolutions {

	public static final int[][] PUZZLE_1_SOLUTION = new int[][] { 
			{ 5, 8, 7, 6, 3, 1, 4, 9, 2 },
			{ 3, 2, 1, 8, 4, 9, 6, 5, 7 }, 
			{ 9, 6, 4, 2, 5, 7, 8, 1, 3 },
			{ 2, 4, 5, 7, 9, 8, 3, 6, 1 }, 
			{ 6, 3, 9, 5, 1, 4, 2, 7, 8 },
			{ 7, 1, 8, 3, 2, 6, 5, 4, 9 }, 
			{ 4, 9, 2, 1, 6, 3, 7, 8, 5 },
			{ 1, 7, 3, 4, 8, 5, 9, 2, 6 }, 
			{ 8, 5, 6, 9, 7, 2, 1, 3, 4 }};

	public static final int[][] PUZZLE_2_SOLUTION = new int[][] { 
			{ 5, 4, 2, 6, 8, 1, 3, 9, 7 },
			{ 1, 3, 8, 9, 2, 7, 5, 4, 6 }, 
			{ 7, 9, 6, 4, 3, 5, 2, 8, 1 },
			{ 8, 6, 1, 5, 7, 2, 4, 3, 9 }, 
			{ 3, 7, 9, 8, 4, 6, 1, 5, 2 },
			{ 2, 5, 4, 3, 1, 9, 7, 6, 8 }, 
			{ 6, 2, 3, 7, 9, 4, 8, 1, 5 },
			{ 9, 8, 7, 1, 5, 3, 6, 2, 4 }, 
			{ 4, 1, 5, 2, 6, 8, 9, 7, 3 }};

	public static final int[][] PUZZLE_3_SOLUTION = new int[][] { 
			{ 2, 1, 4, 9, 7, 8, 3, 6, 5 },
			{ 7, 3, 9, 2, 6, 5, 4, 8, 1 }, 
			{ 6, 8, 5, 3, 1, 4, 7, 9, 2 },
			{ 8, 5, 1, 6, 2, 7, 9, 3, 4 }, 
			{ 4, 2, 3, 8, 9, 1, 5, 7, 6 },
			{ 9, 6, 7, 5, 4, 3, 1, 2, 8 }, 
			{ 3, 4, 8, 7, 5, 2, 6, 1, 9 },
			{ 5, 9, 2, 1, 3, 6, 8, 4, 7 }, 
			{ 1, 7, 6, 4, 8, 9, 2, 5, 3 }};

	public static final int[][] PUZZLE_0_SOLUTION = new int[][] { 
			{ 9, 8, 7, 6, 5, 4, 3, 2, 1 },
			{ 2, 4, 6, 1, 7, 3, 9, 8, 5 }, 
			{ 3, 5, 1, 9, 2, 8, 7, 4, 6 },
			{ 1, 2, 8, 5, 3, 7, 6, 9, 4 }, 
			{ 6, 3, 4, 8, 9, 2, 1, 5, 7 },
			{ 7, 9, 5, 4, 6, 1, 8, 3, 2 }, 
			{ 5, 1, 9, 2, 8, 6, 4, 7, 3 },
			{ 4, 7, 2, 3, 1, 9, 5, 6, 8 }, 
			{ 8, 6, 3, 7, 4, 5, 2, 1, 9 }};

	public static int[][][] ALL_SOLUTIONS = {PUZZLE_1_SOLUTION, PUZZLE_2_SOLUTION, PUZZLE_3_SOLUTION, PUZZLE_0_SOLUTION};

	public static int[][] getSolution(int puzzleNumber) {
		return Puzzles.getPuzzle(puzzleNumber, ALL_SOLUTIONS);
	}
}
