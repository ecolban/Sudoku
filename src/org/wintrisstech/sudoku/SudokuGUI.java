package org.wintrisstech.sudoku;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/**
 * This is the main class of an application that finds solutions
 * to Sudoku puzzles.
 *
 * @author Erik
 */
@SuppressWarnings("serial")
public class SudokuGUI extends JPanel implements Runnable {

    /**
     * The instance of Solver that is used to solve the Sudoku puzzles
     */
    private Solver2 solver = new Solver2();
    /**
     * The puzzle passed to the solver. A 0 is used to represent an
     * empty space.
     */
    private int[][] puzzle = new int[9][9];
    /**
     * A timer that fires at regular intervals at which the puzzle will be
     * displayed to show the state of the solving process.
     */
    private Timer animationTimer = new Timer(30, new ActionListener() {

        public void actionPerformed(ActionEvent e) {
            SudokuGUI.this.repaint();
            if (solver.isDone()) {
                animationTimer.stop();
                startButton.setText("Arm");
                startButton.setEnabled(true);
            }
        }
    });

    /*
     * A button used to control the Sudoku application
     */
    private JButton startButton;
    /**
     * An ActionListener that handles clicks on the start button.
     */
    private ActionListener startButtonListener = new ActionListener() {

        private Thread solverThread;
        private boolean armed = false;
        private int puzzleNumber = 0;

        public void actionPerformed(ActionEvent e) {
            if (armed) {
                armed = false;
                solverThread.start();
                animationTimer.start();
                startButton.setEnabled(false);
            } else if (solverThread == null || !solverThread.isAlive()) {
                if (puzzleNumber >= Puzzles.ALL_PUZZLES.length) {
                    puzzleNumber = 0;
                }
                puzzle = Puzzles.getPuzzle(puzzleNumber++);
                solver.setPuzzle(puzzle);
                solverThread = new Thread(solver);
                startButton.setText("Start");
                armed = true;
                SudokuGUI.this.repaint();
            }
        }
    };

    /**
     * The main method of the application. Dispatches an instance of the
     * SudokuGUI to the Event Dispatch Queue, so that the instance's run()
     * method is invoked from the EventDispatch Thread.
     * @param args ignored
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new SudokuGUI());
    }

    public void run() {
        JFrame frame = new JFrame("Sudoku");
        setPreferredSize(new Dimension(182, 182));
        frame.setLayout(new BorderLayout());
        frame.add(this);
        startButton = new JButton("Arm");
        frame.add(startButton, BorderLayout.SOUTH);
        startButton.addActionListener(startButtonListener);
        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.clearRect(0, 0, getWidth(), getHeight()); // clear all
        int left = (getWidth() - 182) / 2;
        int top = (getHeight() - 182) / 2;
        // Add the thin lines of the grid
        for (int i = 1; i < 182; i += 20) {
            g2.drawLine(left + 1, top + i, left + 181, top + i);
            g2.drawLine(left + i, top + 1, left + i, top + 181);
        }
        g2.setStroke(new BasicStroke(2.0F));
        // Add the thick lines of the grid
        for (int i = 1; i < 182; i += 60) {
            g2.drawLine(left + 1, top + i, left + 181, top + i);
            g2.drawLine(left + i, top + 1, left + i, top + 181);
        }
        // Add the numbers into the cells of the grid
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (puzzle[i][j] != 0) {
                    g2.drawString("" + puzzle[i][j], left + 8 + j * 20, top + 16 + i * 20);
                }
            }
        }
    }
}
