package org.wintrisstech.sudoku;

import java.awt.Point;
import java.util.Random;

/**
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
     * An array of all the index pairs of the spaces in the puzzle.
     * The order in which these pairs occur is not significant, and the array
     * may be shuffled to randomize the solution process
     */
    private Point[] spaces = new Point[9 * 9];
    // A random number generator used to shuffle the spaces array.
    private Random random = new Random();

    /**
     * Solves a SudokuGUI puzzle.
     * <p>
     * The strategy is to prove that there is no solution by
     * exhausting all possible solutions. It invokes the method proveImpossible(),
     * which throws an exception if it, against all odds, discovers a solution.
     *
     */
    @Override
    public void run() {
        long startTime = System.currentTimeMillis();
        // Initialize the spaces array:
        for (int k = 0; k < spaces.length; k++) {
            // Fisher-Yates inside-out shuffle:
            int randIndex = random.nextInt(k + 1);
            spaces[k] = spaces[randIndex];
            spaces[randIndex] = new Point(k / 9, k % 9);
        }
        try {
            proveImpossible();
            System.out.println("This puzzle has no solutions.");
        } catch (SudokuException ex) {
            System.out.println("This puzzle has a solution:");
            System.out.println("Number of iterations = " + iterations);
        } finally {
            done = true;
            System.out.println("Time used = " + (System.currentTimeMillis() - startTime) + " ms.");
        }
    }

    /**
     * Proves that there is no solution for the puzzle.
     * <p>
     * Pre-condition: No number occurs more than once in each row, column, and
     * 3x3 square (a.k.a. region).
     * <p>
     * Post-condition: If this method exits normally (i.e., without throwing an
     * exception), the puzzle with the assignment of values when this method is
     * called is impossible to solve. The variable puzzle is in
     * the same state as prior to the invocation of this method.
     * <p>
     * @throws SudokuException if it fails to prove that there is no solution.
     * In this case, the variable puzzle contains the solution.
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
        /*Following code line is to ensure that the puzzle is in the same state
        as prior to the invokation of this method.*/
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
//    private Point findEmptySpace() throws SudokuException {
//        // Simple (silly) implementation, which returns the first empty space.
//        for (int i = 0; i < 9; i++) {
//            for (int j = 0; j < 9; j++) {
//                if (puzzle[i][j] == 0) {
//                    return new Point(i, j);
//                }
//            }
//        }
//        throw new SudokuException();
//    }
    private Point findEmptySpace() throws SudokuException {
        // This implementation returns a space with least possibilities.
        int minimum = 10;
        Point minSpace = null;
        int i, j;
        for (int k = 0; k < spaces.length; k++) {
            i = spaces[k].x;
            j = spaces[k].y;
            if (puzzle[i][j] == 0) { // An empty space found
                // count the possibilities
                int possibilities = 0;
                for (int n = 1; n < 10; n++) {
                    if (possible(i, j, n)) {
                        possibilities++;
                    }
                }
                if (possibilities < minimum) { 
                    minimum = possibilities; 
                    minSpace = spaces[k]; 
                }
            }
        }
        if (minimum == 10) { // No empty space found
            throw new SudokuException();
        }
        return minSpace;
    }

    /**
     * Tests whether assigning number to puzzle[row][column]
     * introduces the number twice in the same row, column or region.
     * <p>
     * Pre-cond: puzzle[row][column] == 0 and 1 &le; number &le; 9
     * <p>
     * Post-cond: No side effects
     *
     * @param row - row number between 0 and 8
     * @param column - column number between 0 and 8
     * @param number - a number between 1 and 9.
     * @return true if, and only if, assigning puzzle[row][column] = number
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
        int i0 = row - row % 3; // first row index in region
        int j0 = column / 3 * 3; // first column index in region
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
     * Sets the puzzle that this Solver will solve. Note that the solver writes
     * the solution, if any, onto (i.e., modifies) the puzzle argument.
     * Therefore, apply defensive copying to the argument before passing it to
     * this method.
     *
     * @param puzzle the puzzle to solve
     */
    void setPuzzle(int[][] puzzle) {
        this.puzzle = puzzle;
        this.iterations = 0;
        this.done = false;
    }

    /**
     * Test if the solver has completed.
     * @return true is the solver has either found a solution or proven that no
     * solution exists
     */
    boolean isDone() {
        return done;
    }
}
