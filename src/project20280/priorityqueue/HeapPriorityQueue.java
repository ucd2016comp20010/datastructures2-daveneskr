package project20280.priorityqueue;

/*
 */

import project20280.interfaces.Entry;

import javax.sound.sampled.Line;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;


/**
 * An implementation of a priority queue using an array-based heap.
 */

public class HeapPriorityQueue<K, V> extends AbstractPriorityQueue<K, V> {

    protected ArrayList<Entry<K, V>> heap = new ArrayList<>();

    /**
     * Creates an empty priority queue based on the natural ordering of its keys.
     */
    public HeapPriorityQueue() {
        super();
    }

    /**
     * Creates an empty priority queue using the given comparator to order keys.
     *
     * @param comp comparator defining the order of keys in the priority queue
     */
    public HeapPriorityQueue(Comparator<K> comp) {
        super(comp);
    }

    /**
     * Creates a priority queue initialized with the respective key-value pairs. The
     * two arrays given will be paired element-by-element. They are presumed to have
     * the same length. (If not, entries will be created only up to the length of
     * the shorter of the arrays)
     *
     * @param keys   an array of the initial keys for the priority queue
     * @param values an array of the initial values for the priority queue
     */
    public HeapPriorityQueue(K[] keys, V[] values) {
        heapify(keys, values);
    }

    // protected utilities
    protected int parent(int j) {
        return (j-1)/2;
    }

    protected int left(int j) {
        return j*2+1;
    }

    protected int right(int j) {
        return j*2+2;
    }

    protected boolean hasLeft(int j) {
        return heap.size() > j*2+1;
    }

    protected boolean hasRight(int j) {
        return heap.size() > j*2+2;
    }

    /**
     * Exchanges the entries at indices i and j of the array list.
     */
    protected void swap(int i, int j) {
        Entry<K , V> temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);
    }

    /**
     * Moves the entry at index j higher, if necessary, to restore the heap
     * property.
     */
    protected void upheap(int j) {
        int parentIdx;
        while (j > 0 && compare(heap.get(parent(j)), heap.get(j)) > 0) {
            parentIdx = parent(j);
            swap(parentIdx, j);
            j = parentIdx;
        }
    }

    /**
     * Moves the entry at index j lower, if necessary, to restore the heap property.
     */
    protected void downheap(int j) {
        while (true) {
            int left = left(j);
            int right = right(j);
            int min = j;

            if (hasLeft(j) && compare(heap.get(left),
                                    heap.get(min)) < 0) {
                min = left;
            }

            if (hasRight(j) && compare(heap.get(right), 
                                    heap.get(min)) < 0) {
                min = right;
            }

            if (min == j) break;

            swap(min, j);
            j = min;
        }
    }

    /**
     * Moves the entry at index j lower, if necessary, to restore the heap property.
     * Stops at index end
     */
    protected void downheap(int j, int end) {
        while (true) {
            int left = left(j);
            int right = right(j);
            int min = j;

            // check within [0, end)
            if (left < end && compare(heap.get(left), heap.get(min)) < 0) {
                min = left;
            }

            if (right < end && compare(heap.get(right), heap.get(min)) < 0) {
                min = right;
            }

            if (min == j) break;

            swap(j, min);
            j = min;
        }
    }

    /**
     * Performs a bottom-up construction of the heap in linear time.
     */
    protected void heapify() {
        heapify(heap);
    }

    protected void heapify(ArrayList<Entry<K, V>> source) {
        for (int i=0; i< source.size(); i++) {
            heap.set( source.size()-(i+1), source.get(i));
            downheap( source.size()-(i+1));
        }
    }

    protected void heapify(K[] keys, V[] values) {
        int size = Math.min(keys.length, values.length);

        heap.clear();

        for (int i=0; i<size; i++)
            heap.add(new PQEntry<>(keys[i], values[i]));

        for (int j=parent(size()-1); j>=0; j--)
            downheap(j);
    }

    public static Integer[] PQSort(Integer[] arr) {
        HeapPriorityQueue<Integer, Integer> heap = new HeapPriorityQueue<>(arr, arr);

        Integer[] sorted = new Integer[heap.size()];
        for (int i = 0; i < sorted.length; i++) {
            sorted[i] = heap.removeMin().getKey();
        }

        return sorted;
    }

    public static Integer[] heapsort(Integer[] arr) {
        Comparator<Integer> reverseComparator = (a, b) -> Integer.compare(b, a);
        HeapPriorityQueue<Integer, Integer> heap = new HeapPriorityQueue<>(reverseComparator);
        heap.heapify(arr, arr);

        for (int i=heap.size()-1; i>0; i--) {
            heap.swap(0, i);
            heap.downheap(0, i);

        }

        Integer[] sorted = new Integer[heap.size()];
        for (int i = 0; i < heap.size(); i++) {
            sorted[i] = heap.getHeap().get(i).getKey();
        }

        return sorted;
    }

    // public methods

    /**
     * Returns the number of items in the priority queue.
     *
     * @return number of items
     */
    @Override
    public int size() {
        return heap.size();
    }

    /**
     * Returns (but does not remove) an entry with minimal key.
     *
     * @return entry having a minimal key (or null if empty)
     */
    @Override
    public Entry<K, V> min() {
        return isEmpty()? null : heap.get(0);
    }

    /**
     * Inserts a key-value pair and return the entry created.
     *
     * @param key   the key of the new entry
     * @param value the associated value of the new entry
     * @return the entry storing the new key-value pair
     * @throws IllegalArgumentException if the key is unacceptable for this queue
     */
    @Override
    public Entry<K, V> insert(K key, V value) throws IllegalArgumentException {
        PQEntry<K, V> entry = new PQEntry<>(key, value);
        heap.add(entry);
        upheap(heap.size()-1);
        return entry;
    }

    /**
     * Removes and returns an entry with minimal key.
     *
     * @return the removed entry (or null if empty)
     */
    @Override
    public Entry<K, V> removeMin() {
        if (heap.isEmpty()) return null;

        Entry<K, V> min = heap.getFirst();
        swap(0, heap.size() - 1);
        heap.removeLast();

        if (!heap.isEmpty()) {
            downheap(0);
        }

        return min;
    }

    public ArrayList<Entry<K, V>> getHeap() {
        return heap;
    }

    public String toString() {
        return heap.toString();
    }

    /**
     * Used for debugging purposes only
     */
    private void sanityCheck() {
        for (int j = 0; j < heap.size(); j++) {
            int left = left(j);
            int right = right(j);
            //System.out.println("-> " +left + ", " + j + ", " + right);
            Entry<K, V> e_left, e_right;
            e_left = left < heap.size() ? heap.get(left) : null;
            e_right = right < heap.size() ? heap.get(right) : null;
            if (left < heap.size() && compare(heap.get(left), heap.get(j)) < 0) {
                System.out.println("Invalid left child relationship");
                System.out.println("=> " + e_left + ", " + heap.get(j) + ", " + e_right);
            }
            if (right < heap.size() && compare(heap.get(right), heap.get(j)) < 0) {
                System.out.println("Invalid right child relationship");
                System.out.println("=> " + e_left + ", " + heap.get(j) + ", " + e_right);
            }
        }
    }

    public static void main(String[] args) {
        Integer[] rands = new Integer[]{35, 26, 15, 24, 33, 4, 12, 1, 23, 21, 2, 5};
        /*
        HeapPriorityQueue<Integer, Integer> pq = new HeapPriorityQueue<>(rands, rands);

        System.out.println("elements: " + Arrays.toString(rands));
        System.out.println("after adding elements: " + pq);

        while (!pq.isEmpty()) {
            System.out.println("min element: " + pq.removeMin());
            System.out.println("after removeMin: " + pq);
        }

         */

        Integer[] sorted = heapsort(rands);
        for (Integer i : sorted) {
            System.out.print(i + ", ");
        }


        // [             1,
        //        2,            4,
        //   23,     21,      5, 12,
        // 24, 26, 35, 33, 15]
    }
}
