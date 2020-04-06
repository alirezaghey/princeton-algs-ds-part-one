/* *****************************************************************************
 *  Name:              Alireza Ghey
 *  Coursera User ID:  Alireza
 *  Last modified:     3/4/2019
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private final int sideLength; // length of the test grids
    private int doneTrials; // number of trials that have already been doen
    private final double[] res; // result of each trial
    private double storedMean; // to avoid repeated calls to StdStats.mean()
    private double storedStdDev; // to avoid repeated calls to StdStats.stddev()

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) throw new IllegalArgumentException();

        this.sideLength = n;
        this.storedMean = -1.0;
        this.storedStdDev = -1.0;
        this.doneTrials = 0;
        this.res = new double[trials];

        for (int i = 0; i < trials; i++) {
            double currRes = this.test(this.sideLength) / ((double) this.sideLength
                    * this.sideLength);
            this.res[i] = currRes;
            this.doneTrials++;
        }

    }

    private int test(int n) {
        Percolation percolation = new Percolation(n);
        int[] cells = new int[this.sideLength * this.sideLength];
        int indexCells = 0;

        for (int i = 0; i < cells.length; i++) {
            cells[i] = i;
        }
        while (!percolation.percolates()) {
            int randIndex = StdRandom.uniform(indexCells, cells.length);
            int currCell = cells[randIndex];
            cells[randIndex] = cells[indexCells];
            cells[indexCells] = currCell;
            indexCells++;
            int row = currCell / this.sideLength + 1;
            int col = currCell % this.sideLength + 1;
            percolation.open(row, col);
        }
        return indexCells;
    }

    // sample mean of percolation threshold
    public double mean() {
        if (this.storedMean == -1.0) {
            this.storedMean = StdStats.mean(this.res, 0, this.doneTrials);
        }
        return this.storedMean;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        if (this.storedStdDev == -1.0) {
            this.storedStdDev = StdStats.stddev(this.res);
        }
        return this.storedStdDev;
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return this.mean() - (1.96 * this.stddev() / java.lang.Math.sqrt(this.res.length));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return this.mean() + (1.96 * this.stddev() / java.lang.Math.sqrt(this.res.length));
    }

    // test client (see below)
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int t = Integer.parseInt(args[1]);
        PercolationStats percolationStats = new PercolationStats(n, t);
        StdOut.println("mean\t\t\t\t= " + percolationStats.mean());
        StdOut.println("stddev\t\t\t\t= " + percolationStats.stddev());
        StdOut.println("95% confidence interval = [" + percolationStats.confidenceLo() + ", "
                               + percolationStats.confidenceHi() + "]");

    }
}
