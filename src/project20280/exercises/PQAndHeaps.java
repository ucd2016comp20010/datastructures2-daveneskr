package project20280.exercises;

import project20280.priorityqueue.HeapPriorityQueue;

public class PQAndHeaps {
    public static void q1() {
        int[] key = {2, 5, 16, 4, 10, 23, 39, 18, 26, 15};
        HeapPriorityQueue<Integer, Integer> heap = new HeapPriorityQueue<>();
        for (int i : key) {
            heap.insert(i, i);
            System.out.println(heap);
        }
    }

    public static void main(String[] args) {
        q1();
    }
}
