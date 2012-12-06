package org.wintrisstech.sudoku;

/**
 * This is the main (and only) class of an application that finds solutions
 * to Sudoku puzzles.
 *
 * @author Erik
 */
public class SudokuUI {

    /**
     * The puzzle passed to the solver. A 0 is used to represent an
     * empty space.
     */
    private int[][] puzzle;

    public static void main(String[] args) {
        new SudokuUI().run();
    }

    private void run() {
        Solver solver = new Solver();
        for (int i = 0; i < Puzzles.ALL_PUZZLES.length; i++) {
            puzzle = Puzzles.getPuzzle(i);
            solver.setPuzzle(puzzle);
            solver.run();
            printPuzzle();
        }
    }

    /**
     * Prints the puzzle on the terminal.
     */
    private void printPuzzle() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                System.out.print(" " + puzzle[i][j]);
                if (j == 2 || j == 5) {
                    System.out.print(" |");
                }
            }
            System.out.println();
            if (i == 2 || i == 5) {
                System.out.println("------------------------");
            }
        }
    }
}
