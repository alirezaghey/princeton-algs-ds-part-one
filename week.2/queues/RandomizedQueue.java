/* *****************************************************************************
 *  Name:              Alireza Ghey
 *  Coursera User ID:  Alireza
 *  Last modified:     3/4/2019
 *
 * Known Issues:
 *      1- The resizing triggering mechanism may not be optimal
 *      2- Loitering of already dequeued items
 *      3- Some already dequeued items are still returned (not deleted correctly)
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private int capacity = 10;
    private int sz = 0;

    private Item[] items;
    // private int[] indices;
    private int currentIndex = 0;

    // construct an empty randomized queue
    public RandomizedQueue() {
        this.items = (Item[]) new Object[this.capacity];
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return this.size() == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return this.sz;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) throw new IllegalArgumentException();

        this.items[(this.currentIndex + this.size()) % this.capacity] = item;
        this.sz++;
        if (this.size() == this.capacity) {
            this.resize(this.capacity * 2);
        }

    }

    private void resize(int newSize) {
        Item[] newItems = (Item[]) new Object[newSize];
        for (int i = this.currentIndex, j = 0; j < this.size(); i++, j++) {
            newItems[j] = this.items[i % this.capacity];
        }

        this.items = newItems;
        this.capacity = newSize;
        this.currentIndex = 0;
    }

    // remove and return a random item
    public Item dequeue() {
        if (this.size() == 0) throw new NoSuchElementException();

        int randIndex = StdRandom.uniform(this.currentIndex, this.currentIndex + this.size());
        Item item = this.items[randIndex % this.capacity];
        this.items[randIndex % this.capacity] = this.items[this.currentIndex % this.capacity];
        this.items[this.currentIndex % this.capacity] = null;
        this.currentIndex++;
        this.sz--;

        if (this.capacity > 10 && this.size() < this.capacity / 4) {
            this.resize(this.capacity / 2);
        }

        return item;
    }

    private void swap(int[] arr, int left, int right) {
        int temp = arr[left];
        arr[left] = arr[right];
        arr[right] = temp;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (this.size() == 0) throw new NoSuchElementException();

        int randIndex = StdRandom.uniform(this.currentIndex, this.currentIndex + this.size());
        Item item = this.items[randIndex % this.capacity];
        return item;
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        Iterator<Item> iter = new RandomizedQueueIterator(this.currentIndex, this.size(),
                                                          this.items, this.capacity);
        return iter;
    }

    private class RandomizedQueueIterator implements Iterator<Item> {
        private int currIndex;
        private final int[] indices;
        private final int sz;
        private final Item[] items;
        private final int capacity;

        public RandomizedQueueIterator(int currIndex, int sz, Item[] items, int capacity) {
            this.currIndex = 0;
            this.sz = sz;
            this.items = items;
            this.indices = new int[this.sz];
            this.capacity = capacity;

            for (int i = 0; i < indices.length; i++) {
                this.indices[i] = currIndex + i;
            }
        }

        public boolean hasNext() {
            return this.currIndex < this.sz;
        }

        public Item next() {
            if (!this.hasNext()) throw new NoSuchElementException();

            int randIndex = StdRandom.uniform(this.currIndex, this.sz);
            Item item = this.items[this.indices[randIndex] % this.capacity];
            this.swap(this.indices, this.currIndex, randIndex);
            currIndex++;
            return item;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        private void swap(int[] arr, int left, int right) {
            int temp = arr[left];
            arr[left] = arr[right];
            arr[right] = temp;
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<Integer> randQueue = new RandomizedQueue<Integer>();
        for (int i = 0; i < 100; i++) {
            randQueue.enqueue(i);
            StdOut.println("dequeue() => " + randQueue.dequeue());
            randQueue.enqueue(i);
            StdOut.println("sample() => " + randQueue.sample());
            StdOut.println("size() => " + randQueue.size());
        }
    }

}
