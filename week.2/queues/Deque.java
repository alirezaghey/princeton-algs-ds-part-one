/* *****************************************************************************
 *  Name:              Alireza Ghey
 *  Coursera User ID:  Alireza
 *  Last modified:     5/4/2019
 *
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private Node head;
    private Node tail;
    private int sz;

    // construct an empty deque
    public Deque() {
        this.head = null;
        this.tail = null;
        this.sz = 0;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return this.sz == 0;
    }

    // return the number of items on the deque
    public int size() {
        return this.sz;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) throw new IllegalArgumentException();

        Node oldHead = this.head;
        Node newHead = new Node(item);
        this.head = newHead;
        this.head.setNext(oldHead);
        if (this.size() == 0) {
            this.tail = this.head;
        }
        else {
            oldHead.setPrev(this.head);
        }
        this.sz++;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) throw new IllegalArgumentException();

        Node oldTail = this.tail;
        Node newTail = new Node(item);
        this.tail = newTail;
        this.tail.setPrev(oldTail);
        if (this.size() == 0) {
            this.head = this.tail;
        }
        else {
            oldTail.setNext(this.tail);
        }
        this.sz++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (this.size() == 0) throw new NoSuchElementException();

        Item item = this.head.d;
        Node newHead = this.head.getNext();
        this.head.setNext(null);
        this.head = newHead;
        if (this.head != null) {
            this.head.setPrev(null);
        }
        else {
            this.tail = null;
        }
        this.sz--;
        return item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (this.size() == 0) throw new NoSuchElementException();

        Item item = this.tail.d;
        Node newTail = this.tail.getPrev();
        this.tail.setPrev(null);
        this.tail = newTail;
        if (this.tail != null) {
            this.tail.setNext(null);
        }
        else {
            this.head = null;
        }
        this.sz--;
        return item;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new DeqIterator(this.head);
    }

    private class DeqIterator implements Iterator<Item> {
        private Node curr;

        public DeqIterator(Node head) {
            this.curr = head;
        }

        public boolean hasNext() {
            return this.curr != null;
        }

        public Item next() {
            if (!this.hasNext()) throw new NoSuchElementException();

            Item item = curr.d;
            curr = curr.getNext();
            return item;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

    }

    // unit testing (required)
    public static void main(String[] args) {
        int[] testData = new int[10];
        for (int i = 0; i < 10; i++) {
            testData[i] = i;
        }

        Deque<Integer> deq = new Deque<Integer>();
        for (int i : testData) {
            deq.addFirst(i);
            StdOut.println(deq.removeFirst());
            deq.addLast(i);
            StdOut.println(deq.removeLast());
            deq.addFirst(i);
            StdOut.println(deq.size());
        }

        System.out.println("Iterator Check");
        for (int i : deq) {
            System.out.println(i);
        }
    }

    private class Node {
        private final Item d;
        private Node n;
        private Node p;

        public Node(Item d) {
            this.d = d;
            this.n = null;
            this.p = null;
        }

        public Item data() {
            return this.d;
        }

        public Node getNext() {
            return this.n;
        }

        public void setNext(Node node) {
            this.n = node;
        }

        public Node getPrev() {
            return this.p;
        }

        public void setPrev(Node node) {
            this.p = node;
        }

    }

}
