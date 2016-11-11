


import java.awt.Color;
import javax.swing.*;
import java.util.ArrayList;

public class Cell {
    // For keeping track of self.
    private int[] coords; // Grid coords (0-->totalCells-1).
    // Bool values.
    public boolean[] walls = new boolean[4]; // Walls : [Top   , Right , Bottom, Left].
    public boolean solved; // Included in a solution?
    private boolean visited; // Visited in generation?
    // For solving.
    private Cell parent;
    // For calculating heuristics.
    private int hCost; // Manhattan.
    private int gCost; // Movement cost.
    private int fCost; // Total.
    
    public JButton panel;
    public Color color;
    public boolean parentSet;
    // Keep track of how many cells there are. Might be useful.
    public static int numCells = 0;
    
    // Class constructor.
    public Cell(int[] coords, int pixels) {
        this.color = new Color(32,32,32);
        this.coords = coords;
        this.walls[0] = true;
        this.walls[1] = true;
        this.walls[2] = true;
        this.walls[3] = true;
        this.parentSet = false;
        this.fCost = -1;
        numCells++;
    } // End Constructor.
    public ArrayList<Cell> checkNeighbors() {
        ArrayList<Cell> neighbors = new ArrayList<Cell>();
        Cell[][] grid = MazeMaker.grid;
        if (this.coords[0] > 0) {
            if (grid[this.coords[0]-1][this.coords[1]].getVisited() == false) {
                neighbors.add(grid[this.coords[0]-1][this.coords[1]]);
            }
        }
        if (this.coords[0] < grid.length-1) {
            if (grid[this.coords[0]+1][this.coords[1]].getVisited() == false) {
                neighbors.add(grid[this.coords[0]+1][this.coords[1]]);
            }
        }
        if (this.coords[1] > 0) {
            if (grid[this.coords[0]][this.coords[1]-1].getVisited() == false) {
                neighbors.add(grid[this.coords[0]][this.coords[1]-1]);
            }
        }
        if (this.coords[1] < grid[0].length-1) {
            if (grid[this.coords[0]][this.coords[1]+1].getVisited() == false) {
                neighbors.add(grid[this.coords[0]][this.coords[1]+1]);
            }
        }
        return neighbors;
    } // End Method.
    // Method to return valid cells the solver has access to from the current cell.
    public ArrayList<Cell> getValidNeighbors() {
        Cell[][] grid = MazeMaker.grid;
        ArrayList<Cell> neighbors = new ArrayList<Cell>();
        if (!this.walls[0]) {
            neighbors.add(grid[this.coords[0]][this.coords[1]-1]);
        }
        if (!this.walls[1]) {
            neighbors.add(grid[this.coords[0]+1][this.coords[1]]);
        }
        if (!this.walls[2]) {
            neighbors.add(grid[this.coords[0]][this.coords[1]+1]);
        }
        if (!this.walls[3]) {
            neighbors.add(grid[this.coords[0]-1][this.coords[1]]);
        }
        ArrayList<Cell> valid = new ArrayList<Cell>();
        Cell[] neighborArr = neighbors.toArray(new Cell[0]);
        for (int i = 0; i < neighborArr.length; i++) {
            if (!neighborArr[i].solved) {
                valid.add(neighborArr[i]);
            }
        }
        return valid;
    } // End Method.
    public void checkNewParent(Cell parent) {
        if (this.parentSet) {
            if (this.gCost > parent.getGCost() + 1) {
                this.parent = parent;
            } // End if.
        } else {
            this.parent = parent;
        } // End if.
        this.setGCost();
        this.setFCost();
        this.parentSet = true;
    } // End Method.
    public void display() {
        this.panel.setBackground(this.color);
        this.panel.setVisible(true);
    } // End Method.
    // Methods interact with field values.
    // Needed for encapsulation - other classes must go through
    // these methods to modify data held by a Cell object.
    public void setHCost(Cell goal) {
        int xDiff = Math.abs(goal.coords[0] - this.coords[0]);
        int yDiff = Math.abs(goal.coords[1] - this.coords[1]);
        this.hCost = xDiff + yDiff;
    } // End Method.
    public int[] getCoords() {
        return this.coords;
    } // End Method.
    public int getHCost() {
        return this.hCost;
    } // End Method.
    public int getGCost() {
        return this.gCost;
    } // End Method.
    public void setGCost() {
        this.gCost = this.parent.getGCost()+1;
    } // End Method.
    public int getFCost() {
        return this.fCost;
    }
    public void setFCost() {
        this.fCost = this.hCost + this.gCost;
    } // End Method.
    public boolean getVisited() {
        return this.visited;
    } // End Method.
    public boolean getSolved() {
        return this.solved;
    } // End Method.
    public void setVisited(boolean b) {
        this.visited = b;
    } // End Method.
    public void setSolved(boolean b) {
        this.visited = b;
    } // End Method.
    public Cell getParent() {
        return this.parent;
    } // End Method.
} // End Class.
