package sudoku;

import java.awt.Point;

/**
 * This is the main (and only) class of an application that finds solutions
 * to Sudoku puzzles.
 *
 * @author Erik
 */
public class Sudoku {

    /**
     * This puzzle is considered to be a near worst case for brute force
     * algorithms.
     */
    public static int[][] PUZZLE_0 = new int[][]{
        {0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 3, 0, 8, 5},
        {0, 0, 1, 0, 2, 0, 0, 0, 0},
        {0, 0, 0, 5, 0, 7, 0, 0, 0},
        {0, 0, 4, 0, 0, 0, 1, 0, 0},
        {0, 9, 0, 0, 0, 0, 0, 0, 0},
        {5, 0, 0, 0, 0, 0, 0, 7, 3},
        {0, 0, 2, 0, 1, 0, 0, 0, 0},
        {0, 0, 0, 0, 4, 0, 0, 0, 9}};
    /**
     * This is the Sudoku puzzle in San Diego Union Tribune, April 25, 2009:
     */
    public static int[][] PUZZLE_1 = new int[][]{
        {5, 0, 0, 0, 3, 0, 0, 9, 0},
        {0, 2, 1, 0, 0, 9, 0, 0, 0},
        {0, 6, 0, 0, 0, 0, 0, 1, 0},
        {0, 0, 5, 0, 9, 8, 3, 6, 0},
        {6, 0, 0, 0, 1, 0, 0, 0, 8},
        {0, 1, 8, 3, 2, 0, 5, 0, 0},
        {0, 9, 0, 0, 0, 0, 0, 8, 0},
        {0, 0, 0, 4, 0, 0, 9, 2, 0},
        {0, 5, 0, 0, 7, 0, 0, 0, 4}};
    /**
     * This is the Sudoku puzzle in San Diego Union Tribune, May 2, 2009:
     */
    public static int[][] PUZZLE_2 = new int[][]{
        {0, 4, 0, 6, 0, 0, 0, 0, 7},
        {1, 3, 8, 9, 0, 0, 5, 0, 0},
        {7, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 1, 5, 7, 0, 0, 3, 0},
        {3, 0, 0, 0, 0, 0, 0, 0, 2},
        {0, 5, 0, 0, 1, 9, 7, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 5},
        {0, 0, 7, 0, 0, 3, 6, 2, 4},
        {4, 0, 0, 0, 0, 8, 0, 7, 0}};
    /**
     * A representation of a 9x9 Sudoku puzzle. A 0 is used to represent an
     * empty space.
     */
    private int[][] puzzle;

    /**
     * An exception thrown when proving that a puzzle has no solution fails.
     */
    private static class SudokuException extends Exception {
    };
    /**
     * An iteration counter
     */
    private int iterations = 0;

    /**
     * The constructor. It is assumed but not checked that the argument is a valid
     * 9x9 Sudoku puzzle.
     * @param puzzle the puzzle that this instance is solving.
     */
    public Sudoku(int[][] puzzle) {
        this.puzzle = puzzle.clone();
        for (int i = 0; i < 9; i++) { //deep clone
            this.puzzle[i] = puzzle[i].clone();
        }
    }

    /**
     * The main method.
     * @param args - always an empty array
     */
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        new Sudoku(PUZZLE_0).solve();
        System.out.println("Time used = " + (System.currentTimeMillis() - startTime) + " ms.");
    }

    /**
     * Solves a Sudoku puzzle.
     * <p>
     * The strategy is to prove that there is no solution by
     * exhasuting all possible solutions. It invokes the method proveImpossible(),
     * which throws an exception if it, against all odds, discovers a solution.
     *
     */
    private void solve() {
        try {
            proveImpossible();
            System.out.println("This puzzle has no solutions.");
        } catch (SudokuException ex) {
            System.out.println("This puzzle has a solution:");
            printPuzzle();
        }
        System.out.println("Number of iterations = " + iterations);
    }

    /**
     * Proves that there is no solution for the puzzle.
     * <p>
     * Pre-condition: No number occurs more than once in each row, column, and
     * 3x3 square (aka region).
     * <p>
     * Post-condition: If this method exits normally (i.e., without throwing an
     * exception), the puzzle with the assignment of values when this method is
     * called is impossible to solve. The variable puzzle is in
     * the same state as prior to the invocation of this method.
     * <p>
     * @throws SudokuException if it fails to prove that there is no solution.
     */
    private void proveImpossible() throws SudokuException {
        Point p = findEmptySpace(); // Throws an exception if no empty spaces left.
        for (int value = 1; value < 10; value++) {
            iterations++;
            if (possible(p.x, p.y, value)) {
                puzzle[p.x][p.y] = value;
                proveImpossible();
            }
        }
        puzzle[p.x][p.y] = 0;
    }

    /**
     * Returns an empty space in the puzzle, if any.
     * <p>
     * Post-cond: Variable puzzle in same state as prior to call.
     * @return a Point whose coordinates are the row and column of an empty
     * space.
     * @throws SudokuException if puzzle has no empty spaces left.
     */
    private Point findEmptySpace() throws SudokuException {
        // This implementation returns a space with a minimum of possible
        // assignments.
        int minimum = 10;
        int row = 0;
        int column = 0;
        int possibilities;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (puzzle[i][j] == 0) {
                    possibilities = 0;
                    for (int k = 1; k < 10; k++) {
                        if (possible(i, j, k)) {
                            possibilities++;
                        }
                    }
                    if (possibilities < minimum) {
                        minimum = possibilities;
                        row = i;
                        column = j;
                    }
                }
            }
        }
        if (minimum == 10) {
            throw new SudokuException();
        }
        return new Point(row, column);
    }

    /**
     * Tests whether assigning puzzle[row][column] = number
     * introduces the number twice in the same row, column or region.
     * <p>
     * Pre-cond: puzzle[row][column] == 0 and 1 &le; number &le; 9
     * <p>
     * Post-cond: No side effects
     *
     * @param row - row number between 0 and 8
     * @param column - column number between 0 and 8
     * @param number - a number between 1 and 9.
     * @return true if, and only if, assignment puzzle[row][column] = number 
     * will not introduce a duplicate.
     */
    private boolean possible(int row, int column, int number) {
        // Check row and column:
        for (int k = 0; k < 9; k++) {
            if (puzzle[row][k] == number || puzzle[k][column] == number) {
                return false;
            }
        }
        //Check region:
        int i0 = row / 3 * 3;
        int j0 = column / 3 * 3;
        for (int i = i0; i < i0 + 3; i++) {
            for (int j = j0; j < j0 + 3; j++) {
                if (puzzle[i][j] == number) {
                    return false;
                }
            }
        }
        //No duplicates found in row, column or region:
        return true;
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
