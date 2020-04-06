/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Permutation {
    public static void main(String[] args) {
        int size = Integer.parseInt(args[0]);
        RandomizedQueue<String> deq = new RandomizedQueue<String>();
        while (!StdIn.isEmpty()) {
            deq.enqueue(StdIn.readString());
        }
        for (int i = 0; i < size; i++)
            StdOut.println(deq.dequeue());
    }
}
