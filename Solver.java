

import java.awt.Color;
import java.util.ArrayList;

public class Solver {
    // Fields.
    private Cell[][] grid;
    public ArrayList<Cell> openList;
    public ArrayList<Cell> closedList;
    private Cell start;
    private Cell goal;
    private int loopNum;
    // Methods.
    public Solver(Cell start, Cell goal) {
        this.loopNum = 0;
        this.start = start;
        this.goal = goal;
        this.grid = MazeMaker.grid;
        this.closedList = new ArrayList<Cell>();
        this.openList = new ArrayList<Cell>();
    } // End Constructor Method.
    public void solve() {
        this.openList = new ArrayList<Cell>();
        this.closedList = new ArrayList<Cell>();
        for (int x = 0; x < this.grid.length; x++) {
            for (int y = 0; y < this.grid[0].length; y++) {
                this.grid[x][y].setHCost(this.goal);
            }
        }
        this.closedList.add(this.start);
        this.updateOpenList(start);
        while (!this.closedList.contains(this.goal)) {
            Cell[] closedArr = this.closedList.toArray(new Cell[0]);
            for (int i = 0; i < closedArr.length; i++) {
                closedArr[i].color = Color.MAGENTA;
                closedArr[i].display();
                //this.updateOpenList(closedArr[i]);
            }
            Cell[] openArr = this.openList.toArray(new Cell[0]);
            for (int i = 0; i < openArr.length; i++) {
                openArr[i].color = Color.RED;
                openArr[i].display();
            }
            goal.color = Color.YELLOW;
            //goal.display();
            start.color = Color.GREEN;
            //start.display();
            Cell nextCell = this.getLowest(this.openList);
            if (this.openList.contains(this.goal)) {
                nextCell = this.goal;
            }
            this.openList.remove(nextCell);
            this.closedList.add(nextCell);
            nextCell.solved = true;
            this.updateOpenList(nextCell);
            System.out.println("Debug.");
            System.out.print(nextCell.getCoords()[0]);
            System.out.print(", ");
            System.out.println(nextCell.getCoords()[1]);
            loopNum++;
        } // End while.
        System.out.println("Done!");
        this.start.parentSet = false;
        this.tracePath(this.goal);
    } // End method.
    public void updateOpenList(Cell cell) {
        ArrayList<Cell> neighborsL = cell.getValidNeighbors();
        Cell[] neighbors = new Cell[neighborsL.size()];
        for (int i = 0; i < neighborsL.size(); i++) {
            neighbors[i] = neighborsL.get(i);
        }
        System.out.println(neighbors.length);
        for (int i = 0; i < neighbors.length; i++) {
            neighbors[i].checkNewParent(cell);
            if (neighbors[i].getFCost() == -1) {
                neighbors[i].setGCost();
                neighbors[i].setFCost();
            } // End if.
            neighbors[i].checkNewParent(cell);
            if (!this.openList.contains(neighbors[i])) {
                this.openList.add(neighbors[i]);
            } // End if.
        } // End for.
    } // End Method.
    public Cell getLowest(ArrayList<Cell> list) {
        Cell[] array = list.toArray(new Cell[0]);
        Cell lowest = array[0];
        for (int i = 0; i < array.length; i++) {
            if (array[i].getFCost() < lowest.getFCost()) {
                lowest = array[i];
            } // End if.
        } // End for.
        return lowest;
    } // End Method.
    public void tracePath(Cell cell) {
        ArrayList<Cell> path = new ArrayList<Cell>();
        while (cell.parentSet == true) {
            System.out.print(cell.getCoords()[0]);
            System.out.print(", ");
            System.out.println(cell.getCoords()[1]);
            path.add(cell);
            Cell[] pathArr = path.toArray(new Cell[0]);
            for (int i = 0; i < pathArr.length; i++) {
                pathArr[i].color = new Color(128,128,128);
                //pathArr[i].display();
                //pathArr[i].panel.setVisible(false);
                pathArr[i].panel.setOpaque(true);
                pathArr[i].panel.setBackground(new Color(128,128,128));
            } // End for.
            cell = cell.getParent();
            System.out.println("Painting...");
            System.out.println(path.size());
        } // End while.
        System.out.println("Done.");
    } // End Method.
} // End Class.