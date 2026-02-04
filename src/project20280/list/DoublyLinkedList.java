package project20280.list;

import project20280.interfaces.List;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DoublyLinkedList<E> implements List<E> {

    private static class Node<E> {
        private E data;
        private Node<E> next;
        private Node<E> prev;

        public Node(E e, Node<E> p, Node<E> n) {
            data = e;
            prev = p;
            next = n;
        }

        public E getData() {
            return data;
        }

        public Node<E> getNext() {
            return next;
        }

        public Node<E> getPrev() {
            return prev;
        }

        private void setNext(Node<E> n) {next = n;}

        private void setPrev(Node<E> p) {prev = p;}

    }

    private Node<E> head;
    private Node<E> tail;
    private int size = 0;

    public DoublyLinkedList() {
        head = new Node<E>(null, null, null);
        tail = new Node<E>(null, head, null);
        head.next = tail;
    }

    private Node<E> addBetween(E e, Node<E> prev, Node<E> succ) {
        Node<E> newest = new Node<E>(e, prev, succ);

        prev.setNext(newest);
        succ.setPrev(newest);

        size++;

        return newest;
    }

    // assumes correct index
    private Node<E> getNode(int i ) {
        if (isEmpty()) return null;

        Node<E> curr;

        if (i < size/2) {
            curr = head.getNext();
            for (int idx = 0; idx < i; idx++) {
                curr = curr.getNext();
            }
        } else {
            curr = tail.getPrev();
            for (int idx = size-1; idx > i; idx--) {
                curr = curr.getPrev();
            }
        }

        return curr;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public E get(int i) {
        if (i >= size || i < 0) throw new IndexOutOfBoundsException();

        Node<E> n = getNode(i);

        if (n == null) return null;

        return n.getData();
    }

    @Override
    public void add(int i, E e) {
        // first element
        if (isEmpty() && (i == 0)) {
            addBetween(e, head, tail);
            return;
        }

        // last element
        if (i == size) {
            addBetween(e, tail.getPrev(), tail);
            return;
        }

        // in between
        if (size < i || i < 0) throw new IndexOutOfBoundsException();

        Node<E> n = getNode(i);

        addBetween(e, n.getPrev(), n);
    }

    @Override
    public E remove(int i) {
        if (size <= i || i < 0) throw new IndexOutOfBoundsException();

        Node<E> n = getNode(i);

        return remove(n);
    }

    private class DoublyLinkedListIterator<E> implements Iterator<E> {
        Node<E> curr = (Node<E>) head.next;

        @Override
        public boolean hasNext() {
            return curr != tail;
        }

        @Override
        public E next() {
            E res = curr.data;
            curr = curr.next;
            return res;
        }
    }

    @Override
    public Iterator<E> iterator() {
        return new DoublyLinkedListIterator<E>();
    }

    private E remove(Node<E> n) {
        n.getPrev().setNext(n.getNext());
        n.getNext().setPrev(n.getPrev());
        size--;
        return n.getData();
    }

    public E first() {
        if (isEmpty()) {
            return null;
        }
        return head.getNext().getData();
    }

    public E last() {
        if (isEmpty()) {
            return null;
        }
        return tail.getPrev().getData();
    }

    @Override
    public E removeFirst() {
        return remove(0);
    }

    @Override
    public E removeLast() {
        return remove(size-1);
    }

    @Override
    public void addLast(E e) {
        if (isEmpty()) {
            addFirst(e);
            return;
        }
        add(size, e);
    }

    @Override
    public void addFirst(E e) {
        add(0, e);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node<E> curr = head.next;
        while (curr != tail) {
            sb.append(curr.data);
            curr = curr.next;
            if (curr != tail) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    public static void main(String[] args) {
        DoublyLinkedList<Integer> ll = new DoublyLinkedList<>();
        for (int i = 0; i < 5; ++i) ll.addLast(i);

        ll.remove(1);
    }
}