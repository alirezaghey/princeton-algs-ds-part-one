/* *****************************************************************************
 *  Name: Alireza Ghey
 *  Date:
 *  Description:
 **************************************************************************** */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Board {
    private final int[][] grid;
    private final int size;
    private final int hammingDist;
    private final int manhattenDist;
    private int zeroRow;
    private int zeroCol;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        size = tiles.length;
        this.grid = new int[this.size][this.size];
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles.length; j++) {
                this.grid[i][j] = tiles[i][j];
                if (this.grid[i][j] == 0) {
                    zeroRow = i;
                    zeroCol = j;
                }
            }
        }

        hammingDist = calcHammingDist();
        manhattenDist = calcManhattenDist();

    }

    // string representation of this board
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.size + "\n");
        for (int row = 0; row < this.grid.length; row++) {
            for (int col = 0; col < this.grid[row].length; col++) {
                sb.append(String.format("%2d", this.grid[row][col]));
                if (col < this.grid[row].length - 1)
                    sb.append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    // board dimension n
    public int dimension() {
        return this.size;
    }

    private int calcHammingDist() {
        int currDist = 0;
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[row].length; col++) {
                if (grid[row][col] == 0) continue;
                if (grid[row][col] - 1 != row * dimension() + col) currDist++;
            }
        }
        return currDist;
    }

    // number of tiles out of place
    public int hamming() {
        return hammingDist;
    }

    private int calcManhattenDist() {
        int currDist = 0;
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[row].length; col++) {
                int currNum = grid[row][col];
                if (currNum == 0) continue;
                if (currNum - 1 != row * dimension() + col) {
                    currDist += Math.abs(((currNum - 1) / dimension()) - row);
                    currDist += Math.abs(((currNum - 1) % dimension()) - col);
                }
            }
        }
        return currDist;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        return manhattenDist;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hammingDist == 0;
    }

    // does this board equal y?
    public boolean equals(Object other) {
        if (other == this) return true;
        if (other == null) return false;
        if (other.getClass() != this.getClass()) return false;
        Board that = (Board) other;
        if (this.size != that.size) return false;
        for (int row = 0; row < this.grid.length; row++) {
            for (int col = 0; col < this.grid[row].length; col++) {
                if (this.grid[row][col] != that.grid[row][col]) return false;
            }
        }
        return true;

    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        List<Board> iterable = new ArrayList<>();
        int[][] directions = { { -1, 0 }, { 0, 1 }, { 1, 0 }, { 0, -1 } };
        for (int[] dir : directions) {
            int row = zeroRow + dir[0];
            int col = zeroCol + dir[1];
            if (isValidCoordinate(row, col)) {
                int[][] copiedGrid = Arrays.stream(grid).map(int[]::clone).toArray(int[][]::new);
                copiedGrid[zeroRow][zeroCol] = copiedGrid[row][col];
                copiedGrid[row][col] = 0;
                Board copiedBoard = new Board(copiedGrid);
                iterable.add(copiedBoard);
            }
        }
        return iterable;

    }

    private boolean isValidCoordinate(int row, int col) {
        return row >= 0
                && row < this.size
                && col >= 0
                && col < this.size;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int[][] copiedGrid = Arrays.stream(grid).map(int[]::clone).toArray(int[][]::new);
        for (int[] row : copiedGrid) {
            if (row[0] != 0 && row[1] != 0) {
                int temp = row[0];
                row[0] = row[1];
                row[1] = temp;
                break;
            }
        }
        return new Board(copiedGrid);
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        System.out.println("Hello World!");
    }
}
