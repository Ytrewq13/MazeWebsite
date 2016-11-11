

import javax.swing.BorderFactory;
import java.awt.*;
import java.awt.event.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.border.Border;
import java.util.ArrayList;
import java.util.Stack;

public class MazeMaker extends JApplet {
    // Global constants.
    public static final int PIXELS = 10;
    public static final int XDIM = 960;
    public static final int YDIM = 540;
    public static final int CELLSX = Math.floorDiv(XDIM, PIXELS);
    public static final int CELLSY = Math.floorDiv(YDIM, PIXELS);
    public static final int TIMESTEP = 0;
    public static final int TIMESOLVE = 0;
    
    public static JFrame wFrame;
    public static JPanel window;
    public static JPanel buttonPanel;
    public static Cell[][] grid;
    
    public static int loopNum;
    
    public static Stack<Cell> stack = new Stack<Cell>();

    public static void main(String[] args) {
        MazeMaker.loopNum = 0;
        Border border = BorderFactory.createMatteBorder(1,1,1,1,Color.GREEN);
        // Setup the window.
        wFrame = new JFrame("Insert Maze pun here.");
        JPanel wContainer = new JPanel(new BorderLayout());
        wFrame.setMinimumSize(new Dimension(XDIM,YDIM));
        window = new JPanel(new GridLayout(CELLSY, CELLSX));
        window.setSize(XDIM, YDIM);
        wContainer.add(window, BorderLayout.CENTER);
        wFrame.add(wContainer);
        grid = MazeMaker.makeGrid();
        for (int y = 0; y < grid[0].length; y++) {
            for(int x = 0; x < grid.length; x++) {
                grid[x][y].panel = new JButton();
                grid[x][y].panel.putClientProperty("x", x);
                grid[x][y].panel.putClientProperty("y", y);
                grid[x][y].panel.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent ae) {
                        for (int x = 0; x < grid.length; x++) {
                            for (int y = 0; y < grid[x].length; y++) {
                                grid[x][y].solved = false;
                                grid[x][y].color = new Color(64,64,64);
                                grid[x][y].panel.setBackground(new Color(64,64,64));
                                grid[x][y].display();
                            }
                        }
                        display(grid);
                        Cell start = grid[0][0];
                        JButton btn = (JButton)ae.getSource();
                        int btnx = (int)btn.getClientProperty("x");
                        int btny = (int)btn.getClientProperty("y");
                        Cell goal = grid[btnx][btny];
                        Solver solver = new Solver(start, goal);
                        solver.solve();
                    }
                });
                window.add(grid[x][y].panel);
                Border paneEdge = BorderFactory.createMatteBorder(1,1,1,1, Color.GREEN);
                grid[x][y].panel.setBorder(paneEdge);
                grid[x][y].panel.setVisible(true);
            }
        }
        wFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Cell current = grid[0][0];
        MazeMaker maze = new MazeMaker();
        current = maze.makeMaze(current);
        while (!stack.isEmpty()) {
            current = maze.makeMaze(current);
        }
        for (int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid[0].length; y++) {
                grid[x][y].color = new Color(64,64,64);
            }
        }
        MazeMaker.display(grid);
    } // End main method.
    public Cell makeMaze(Cell current) {
        MazeMaker.loopNum++;
        //Logic.
        current.setVisited(true);
        Cell next;
        ArrayList<Cell> neighbors = current.checkNeighbors();
        if (!neighbors.isEmpty()) {
            // There are valid neighbors.
            stack.push(current);
            next = neighbors.toArray(new Cell[0])[((int) Math.floor(Math.random() * neighbors.size()))];
            MazeMaker.removeWalls(current, next);
        } else {
            // Backtrack here.
            next = stack.pop();
        }
        // Graphics.
        if (TIMESTEP != 0) {
            if (loopNum % TIMESTEP == 0) {
                MazeMaker.display(grid);
            }
        }
        return next;
    }
    // Make the empty grid ready for maze generation.
    public static Cell[][] makeGrid() {
        Cell[][] cellArray = new Cell[CELLSX][CELLSY];
        for (int x = 0; x < CELLSX; x++) {
            for (int y = 0; y < CELLSY; y++) {
                int[] coords = {x,y};
                cellArray[x][y] = new Cell(coords, PIXELS);
            } // End for.
        } // End for.
        return cellArray;
    } // End method.
    
    public static void display(Cell[][] grid) {
        for (int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid[x].length; y++) {
                int top = 0;
                int right = 0;
                int bottom = 0;
                int left = 0;
                if (grid[x][y].walls[0]) {
                    top = 1;
                }
                if (grid[x][y].walls[1]) {
                    right = 1;
                }
                if (grid[x][y].walls[2]) {
                    bottom = 1;
                }
                if (grid[x][y].walls[3]) {
                    left = 1;
                }
                Border paneEdge = BorderFactory.createMatteBorder(top,left,bottom,right, Color.green);
                grid[x][y].panel.setBorder(paneEdge);
                grid[x][y].panel.setBackground(grid[x][y].color);
            } // End for.
        } // End for.
        //wFrame.setSize(XDIM, YDIM);
        //wFrame.pack();
        wFrame.setVisible(true);
        window.setVisible(true);
    } // End Method.
    public static void removeWalls(Cell cell1, Cell cell2) {
        if (cell1.getCoords()[0] == cell2.getCoords()[0]) {
            if (cell1.getCoords()[1] > cell2.getCoords()[1]) {
                cell1.walls[0] = false;
                cell2.walls[2] = false;
            } else {
                cell1.walls[2] = false;
                cell2.walls[0] = false;
            }
        } else {
            if (cell1.getCoords()[0] > cell2.getCoords()[0]) {
                cell1.walls[3] = false;
                cell2.walls[1] = false;
            } else {
                cell1.walls[1] = false;
                cell2.walls[3] = false;
            }
        }
    }    
} // End class.
