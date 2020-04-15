/* *****************************************************************************
 *  Name: Alireza Ghey
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.MinPQ;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Solver {
    private class Step implements Comparable<Step> {
        private final Step prev;
        private final int currMoves;
        private final Board currBoard;

        public Step(Board b, Step prev, int currMoves) {
            this.prev = prev;
            this.currMoves = currMoves;
            this.currBoard = b;
        }

        public Step previousStep() {
            return prev;
        }

        public int numOfMoves() {
            return currMoves;
        }

        public Board board() {
            return currBoard;
        }

        public int compareTo(Step other) {
            int thisVal = board().manhattan() + numOfMoves();
            int otherVal = other.board().manhattan() + other.numOfMoves();
            return thisVal - otherVal;
        }
    }


    private final boolean solvable;
    private int numOfMoves = 0;
    private final Iterable<Board> solutionPath;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException();

        Board initialBoard = initial;
        Board twinBoard = initialBoard.twin();
        MinPQ<Step> minPQOriginal = new MinPQ<Step>();
        minPQOriginal.insert(new Step(initialBoard, null, 0));
        MinPQ<Step> minPQTwin = new MinPQ<Step>();
        minPQTwin.insert(new Step(twinBoard, null, 0));

        while (true) {
            Step originalStep = minPQOriginal.delMin();
            if (originalStep.board().hamming() == 0) {
                this.solvable = true;
                this.numOfMoves = originalStep.numOfMoves();
                this.solutionPath = this.getSolutionPath(originalStep);
                break;
            }
            for (Board neighbour : originalStep.board().neighbors()) {
                if (originalStep.previousStep() != null
                        && originalStep.previousStep().board().equals(neighbour))
                    continue;
                minPQOriginal
                        .insert(new Step(neighbour, originalStep, originalStep.numOfMoves() + 1));
            }

            Step twinStep = minPQTwin.delMin();
            if (twinStep.board().hamming() == 0) {
                this.solvable = false;
                this.numOfMoves = -1;
                this.solutionPath = null;
                break;
            }
            for (Board neighbour : twinStep.board().neighbors()) {
                if (twinStep.previousStep() != null
                        && twinStep.previousStep().board().equals(neighbour))
                    continue;
                minPQTwin.insert(new Step(neighbour, twinStep, twinStep.numOfMoves() + 1));
            }
        }


    }

    private Iterable<Board> getSolutionPath(Step originalStep) {
        List<Board> path = new ArrayList<Board>();
        while (originalStep != null) {
            path.add(originalStep.board());
            originalStep = originalStep.previousStep();
        }
        Collections.reverse(path);
        return path;
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return solvable;
    }

    // min number of moves to solve initial board
    public int moves() {
        return numOfMoves;
    }

    // sequence of boards in a shortest solution
    public Iterable<Board> solution() {
        return solutionPath;
    }

    // test client (see below)
    public static void main(String[] args) {
        System.out.println("Hello World!");
    }

}
