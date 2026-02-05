package project20280.stacksqueues;

import project20280.interfaces.Queue;
import project20280.list.CircularlyLinkedList;

/**
 * Realization of a circular FIFO queue as an adaptation of a
 * CircularlyLinkedList. This provides one additional method not part of the
 * general Queue interface. A call to rotate() is a more efficient simulation of
 * the combination enqueue(dequeue()). All operations are performed in constant
 * time.
 */

public class LinkedCircularQueue<E> implements Queue<E> {

    CircularlyLinkedList<E> ll;

    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }

    public LinkedCircularQueue() {
        ll = new CircularlyLinkedList<>();
    }

    @Override
    public int size() {
        return ll.size();
    }

    @Override
    public boolean isEmpty() {
        return ll.isEmpty();
    }

    @Override
    public void enqueue(E e) {
        ll.addLast(e);
    }

    @Override
    public E first() {
        return ll.get(0);
    }

    @Override
    public E dequeue() {
        return ll.removeFirst();
    }

    public void rotate() {
        ll.rotate();
    }

}
