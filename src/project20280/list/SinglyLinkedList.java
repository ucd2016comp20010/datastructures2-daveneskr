package project20280.list;

import project20280.interfaces.List;

import java.util.Iterator;

public class SinglyLinkedList<E> implements List<E> {

    private static class Node<E> {

        private final E element;            // reference to the element stored at this node

        /**
         * A reference to the subsequent node in the list
         */
        private Node<E> next;         // reference to the subsequent node in the list

        /**
         * Creates a node with the given element and next node.
         *
         * @param e the element to be stored
         * @param n reference to a node that should follow the new node
         */
        public Node(E e, Node<E> n) {
            element = e;
            next = n;
        }

        // Accessor methods

        /**
         * Returns the element stored at the node.
         *
         * @return the element stored at the node
         */
        public E getElement() {
            return element;
        }

        /**
         * Returns the node that follows this one (or null if no such node).
         *
         * @return the following node
         */
        public Node<E> getNext() {
            return next;
        }

        // Modifier methods

        /**
         * Sets the node's next reference to point to Node n.
         *
         * @param n the node that should follow this one
         */
        public void setNext(Node<E> n) {
            next = n;
        }
    } //----------- end of nested Node class -----------

    /**
     * The head node of the list
     */
    private Node<E> head = null;               // head node of the list (or null if empty)


    /**
     * Number of nodes in the list
     */
    private int size = 0;                      // number of nodes in the list

    public SinglyLinkedList() {
    }              // constructs an initially empty list

    //@Override
    public int size() {
        return size;
    }

    //@Override
    public boolean isEmpty() {
        return head == null;
    }

    @Override
    public E get(int position) {
        if (size <= position) throw new IndexOutOfBoundsException(); // check if index valid

        Node<E> curr = head;

        while (position != 0) {
            curr = curr.getNext();
            position--;
        }

        return curr.getElement();
    }

    @Override
    public void add(int position, E e) {
        if (size <= position) throw new IndexOutOfBoundsException(); // check if index valid

        Node<E> curr = head;
        Node<E> prev = head;

        while (position != 0) {
            prev = curr;
            curr = curr.getNext();
            position--;
        }

        Node<E> newest = new Node<>(e, curr);
        prev.setNext(newest);
        size++;
    }


    @Override
    public void addFirst(E e) {
        Node<E> newest = new Node(e, head);
        head = newest;
        size++;
    }

    @Override
    public void addLast(E e) {
        if (head == null) {
            head = new Node(e, null);
            size++;
            return;
        }

        Node<E> curr = head;

        while (curr.getNext() != null) {
            curr = curr.getNext();
        }

        curr.setNext(new Node<>(e, null));
        size++;
    }

    @Override
    public E remove(int position) {
        if (size <= position) throw new IndexOutOfBoundsException(); // check if index valid

        Node<E> curr = head;
        Node<E> prev = head;

        while (position != 0) {
            prev = curr;
            curr = curr.getNext();
            position--;
        }

        prev.setNext(curr.getNext());
        size--;
        return curr.getElement();
    }

    @Override
    public E removeFirst() {
        if (head == null) return null;
        Node<E> tmp = head;
        head = head.getNext();
        size--;
        return tmp.getElement();
    }

    @Override
    public E removeLast() {
        if (head == null) throw new IndexOutOfBoundsException();

        if (head.getNext() == null) {
            E el = head.getElement();
            head = null;
            size--;
            return el;
        }

        Node<E> curr = head;
        Node<E> prev = head;

        while (curr.getNext() != null) {
            prev = curr;
            curr = curr.getNext();
        }

        prev.setNext(null);

        size--;
        return curr.getElement();
    }

    //@Override
    public Iterator<E> iterator() {
        return new SinglyLinkedListIterator<E>();
    }

    private class SinglyLinkedListIterator<E> implements Iterator<E> {
        Node<E> curr = (Node<E>) head;

        @Override
        public boolean hasNext() {
            return curr != null;
        }

        @Override
        public E next() {
            E res = curr.getElement();
            curr = curr.next;
            return res;
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node<E> curr = head;
        while (curr != null) {
            sb.append(curr.getElement());
            if (curr.getNext() != null)
                sb.append(", ");
            curr = curr.getNext();
        }
        sb.append("]");
        return sb.toString();
    }

    public static void main(String[] args) {
        SinglyLinkedList<Integer> ll = new SinglyLinkedList<Integer>();
        System.out.println("ll " + ll + " isEmpty: " + ll.isEmpty());


        ll.addFirst(0);
        System.out.println("ll " + ll + " isEmpty: " + ll.isEmpty());

        ll.removeLast();
        System.out.println("ll " + ll + " isEmpty: " + ll.isEmpty());

    }
}
