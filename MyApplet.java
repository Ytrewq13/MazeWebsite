

import javax.swing.BorderFactory;
import java.awt.*;
import java.awt.event.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.border.Border;
import java.util.ArrayList;
import java.util.Stack;

public class MyApplet extends JApplet {
    public static JPanel window;
    public void init() {
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                public void run() {
                    MazeMaker.loopNum = 0;
                    Border border = BorderFactory.createMatteBorder(1,1,1,1,Color.GREEN);
                    // Setup the window.
                    JPanel wContainer = new JPanel(new BorderLayout());
                    JPanel window = new JPanel(new GridLayout(MazeMaker.CELLSY, MazeMaker.CELLSX));
                    window.setSize(MazeMaker.XDIM, MazeMaker.YDIM);
                    wContainer.add(window, BorderLayout.CENTER);
                    add(wContainer);
                    MazeMaker.grid = MazeMaker.makeGrid();
                    
                    for (int y = 0; y < MazeMaker.grid[0].length; y++) {
                        for(int x = 0; x < MazeMaker.grid.length; x++) {
                            MazeMaker.grid[x][y].panel = new JButton();
                            MazeMaker.grid[x][y].panel.putClientProperty("x", x);
                            MazeMaker.grid[x][y].panel.putClientProperty("y", y);
                            MazeMaker.grid[x][y].panel.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent ae) {
                                    for (int x = 0; x < MazeMaker.grid.length; x++) {
                                        for (int y = 0; y < MazeMaker.grid[x].length; y++) {
                                            MazeMaker.grid[x][y].solved = false;
                                            MazeMaker.grid[x][y].color = new Color(64,64,64);
                                            MazeMaker.grid[x][y].panel.setBackground(new Color(64,64,64));
                                        }
                                    }
                                    displayApplet(MazeMaker.grid);
                                    Cell start = MazeMaker.grid[0][0];
                                    JButton btn = (JButton)ae.getSource();
                                    int btnx = (int)btn.getClientProperty("x");
                                    int btny = (int)btn.getClientProperty("y");
                                    Cell goal = MazeMaker.grid[btnx][btny];
                                    Solver solver = new Solver(start, goal);
                                    solver.solve();
                                }
                            });
                            window.add(MazeMaker.grid[x][y].panel);
                            Border paneEdge = BorderFactory.createMatteBorder(1,1,1,1, Color.GREEN);
                            MazeMaker.grid[x][y].panel.setBorder(paneEdge);
                            MazeMaker.grid[x][y].panel.setVisible(true);
                        }
                    }
                    Cell current = MazeMaker.grid[0][0];
                    MazeMaker maze = new MazeMaker();
                    current = maze.makeMaze(current);
                    while (!MazeMaker.stack.isEmpty()) {
                        current = maze.makeMaze(current);
                    }
                    for (int x = 0; x < MazeMaker.grid.length; x++) {
                        for (int y = 0; y < MazeMaker.grid[0].length; y++) {
                            MazeMaker.grid[x][y].color = new Color(64,64,64);
                        }
                    }
                    MyApplet.displayApplet(MazeMaker.grid);
                }
            });
        } catch (Exception e) {
            System.err.println("createGUI didn't complete successfully");
        }
    }
    
    public void start() {
        
    }
    
    public static void displayApplet(Cell[][] grid) {
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
    } // End Method.
}
