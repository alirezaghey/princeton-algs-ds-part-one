/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class Permutation {
    public static void main(String[] args) {
        int size = Integer.parseInt(args[0]);
        RandomizedQueue<String> deq = new RandomizedQueue<String>();
        for (int i = 0; i < size; i++) {
            deq.enqueue(StdIn.readString());
        }
        int front = size;
        while (!StdIn.isEmpty()) {
            String s = StdIn.readString();
            front++;
            int randIndex = StdRandom.uniform(0, front);
            if (randIndex < size) {
                deq.dequeue();
                deq.enqueue(s);
            }
        }
        for (int i = 0; i < size; i++)
            StdOut.println(deq.dequeue());
    }
}
