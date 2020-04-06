/* *****************************************************************************
 *  Name:              Alireza Ghey
 *  Coursera User ID:  Alireza
 *  Last modified:     3/4/2019
 *
 * Known Issues: Backwash due to incorrect use of virtual sites
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private WeightedQuickUnionUF uf; // union-find ds to track connection between cells
    private int sideLength; // length of the grid
    private int numOfOpenSites; // number of open cells so far
    // whether a cell is open or closed
    // 0 means closed; 1 means open
    private int[][] cellStatus;

    // constructor
    // takes the sidelength of the percolation grid
    public Percolation(int n) {
        if (n <= 0) throw new IllegalArgumentException();

        this.sideLength = n;
        this.numOfOpenSites = 0;

        // by convention the first and the last elements are virtual sites
        // for the first and last row
        // the virtual sites are always open
        // when opening a cell on the first and last row, we must take care
        // to union them with their corresponding virtual sites
        // to check for percolation we only test if the top virtual site
        // is connected to the bottom virtual site
        // to test if a cell isFull, we check if it is connected to the top
        // virtual site
        this.uf = new WeightedQuickUnionUF(this.sideLength * this.sideLength + 2);

        // 0 closed and 1 means open
        this.cellStatus = new int[n][n];
    }

    // opens the site (row, col) if fit is not open already
    public void open(int row, int col) {
        if (!this.isInGridBoundary(row, col))
            throw new IllegalArgumentException();

        if (this.isOpen(row, col)) return;

        // Make row and column 0-based
        row = row - 1;
        col = col - 1;
        this.cellStatus[row][col] = 1;
        this.numOfOpenSites++;

        // Check all four sides and update if there is a way
        // We need add one to col to compensate for the virtual site at the
        // beginning of the data structure (only necessary for union-find)
        int currCell = row * this.sideLength + col + 1;
        // left
        if (this.isCellValidAndOpen(row, col - 1))
            this.uf.union(row * this.sideLength + col, currCell);
        // top
        // if it's in the top row connect it to virtual site
        if (row == 0) {
            this.uf.union(0, currCell);
        }
        else if (this.isCellValidAndOpen(row - 1, col))
            this.uf.union((row - 1) * this.sideLength + col + 1, currCell);
        // right
        if (this.isCellValidAndOpen(row, col + 1))
            this.uf.union(row * this.sideLength + col + 2, currCell);
        // bottom
        // if it's in the bottom row connect it to virtual site
        if (row == this.sideLength - 1) {
            this.uf.union(this.sideLength * this.sideLength + 1, currCell);
        }
        else if (this.isCellValidAndOpen(row + 1, col))
            this.uf.union((row + 1) * this.sideLength + col + 1, currCell);
    }


    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (!this.isInGridBoundary(row, col))
            throw new IllegalArgumentException();

        return this.cellStatus[row - 1][col - 1] == 1;
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (!this.isInGridBoundary(row, col))
            throw new IllegalArgumentException();
        int indexCurrRow = (row - 1) * this.sideLength + col;
        return this.uf.connected(0, indexCurrRow);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return this.numOfOpenSites;
    }

    // does the system percolate?
    public boolean percolates() {
        return this.uf.connected(0, this.sideLength * this.sideLength + 1);
    }

    // checks whether a cell is in the grid boundary and open
    // zero-based
    private boolean isCellValidAndOpen(int row, int col) {
        if (row >= 0 && row < this.sideLength && col >= 0 && col < this.sideLength
                && this.cellStatus[row][col] == 1)
            return true;
        return false;
    }

    // checks whether row and column are in the grid boundary
    // one-based
    private boolean isInGridBoundary(int row, int col) {
        if (row < 1 || row > this.sideLength || col < 1 || col > this.sideLength)
            return false;
        return true;
    }

    // test client (optional)
    public static void main(String[] args) {

    }


}
