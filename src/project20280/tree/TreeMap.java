package project20280.tree;

import project20280.interfaces.Entry;
import project20280.interfaces.List;
import project20280.interfaces.Position;

import java.util.*;
import java.util.function.Consumer;

/**
 * An implementation of a sorted map using a binary search tree.
 */

public class TreeMap<K, V> extends AbstractSortedMap<K, V> {

    // ---------------- nested BalanceableBinaryTree class ----------------

    /**
     * A specialized version of the LinkedBinaryTree class with additional mutators
     * to support binary search tree operations, and a specialized node class that
     * includes an auxiliary instance variable for balancing data.
     */
    protected static class BalanceableBinaryTree<K, V> extends LinkedBinaryTree<Entry<K, V>> {
        // -------------- nested BSTNode class --------------
        // this extends the inherited LinkedBinaryTree.Node class
        protected static class BSTNode<E> extends Node<E> {
            int aux = 0;

            BSTNode(E e, Node<E> parent, Node<E> leftChild, Node<E> rightChild) {
                super(e, parent, leftChild, rightChild);
            }

            public int getAux() {
                return aux;
            }

            public void setAux(int value) {
                aux = value;
            }
        } // --------- end of nested BSTNode class ---------

        // positional-based methods related to aux field
        public int getAux(Position<Entry<K, V>> p) {
            return ((BSTNode<Entry<K, V>>) p).getAux();
        }

        public void setAux(Position<Entry<K, V>> p, int value) {
            ((BSTNode<Entry<K, V>>) p).setAux(value);
        }

        // Override node factory function to produce a BSTNode (rather than a Node)
        @Override
        protected Node<Entry<K, V>> createNode(Entry<K, V> e, Node<Entry<K, V>> parent, Node<Entry<K, V>> left,
                                               Node<Entry<K, V>> right) {
            return new BSTNode<>(e, parent, left, right);
        }

        /**
         * Relinks a parent node with its oriented child node.
         */
        private void relink(Node<Entry<K, V>> parent,
                            Node<Entry<K, V>> child,
                            boolean makeLeftChild) {

            if (makeLeftChild) {
                parent.setLeft(child);
            } else {
                parent.setRight(child);
            }

            if (child != null) {
                child.setParent(parent);
            }
        }

        /**
         * Rotates Position p above its parent. Switches between these configurations,
         * depending on whether p is a or p is b.
         *
         * <pre>
         *          b                  a
         *         / \                / \
         *        a  t2             t0   b
         *       / \                    / \
         *      t0  t1                 t1  t2
         * </pre>
         * <p>
         * Caller should ensure that p is not the root.
         */
        public void rotate(Position<Entry<K, V>> p) {
            Node<Entry<K, V>> x = (Node<Entry<K, V>>) p;
            Node<Entry<K, V>> y = (Node<Entry<K, V>>) parent(p);

            if (isLeftChild(x)) {
                rightRotate(y);
            } else {
                leftRotate(y);
            }
        }

        private void leftRotate(Node<Entry<K, V>> a) {
            Node<Entry<K, V>> b = (Node<Entry<K, V>>) right(a);
            Node<Entry<K, V>> t1 = b.getLeft();

            relink((Node<Entry<K, V>>) parent(a), b, isLeftChild(a));

            relink(a, t1, false);   // a.right = t1
            relink(b, a, true);     // b.left = a
        }

        private void rightRotate(Node<Entry<K, V>> b) {
            Node<Entry<K, V>> a = (Node<Entry<K,V>>) left(b);
            Node<Entry<K, V>> t1 = a.getRight();

            relink((Node<Entry<K,V>>) parent(b), a, isLeftChild(b));

            relink(b, t1, true); // b.left = t1
            relink(a, b, false); // a.right = b
        }

        /**
         * Returns the Position that becomes the root of the restructured subtree.
         * <p>
         * Assumes the nodes are in one of the following configurations:
         *
         * <pre>
         *     z=a                 z=c           z=a               z=c
         *    /  \                /  \          /  \              /  \
         *   t0  y=b             y=b  t3       t0   y=c          y=a  t3
         *      /  \            /  \               /  \         /  \
         *     t1  x=c         x=a  t2            x=b  t3      t0   x=b
         *        /  \        /  \               /  \              /  \
         *       t2  t3      t0  t1             t1  t2            t1  t2
         * </pre>
         * <p>
         * The subtree will be restructured so that the node with key b becomes its
         * root.
         *
         * <pre>
         *           b
         *         /   \
         *       a       c
         *      / \     / \
         *     t0  t1  t2  t3
         * </pre>
         * <p>
         * Caller should ensure that x has a grandparent.
         */
        public Position<Entry<K, V>> restructure(Position<Entry<K, V>> x) {
            Position<Entry<K, V>> y = parent(x);
            Position<Entry<K, V>> z = parent(y);

            if ((x == left(y) && y == left(z)) ||
                    (x == right(y) && y == right(z))) {
                rotate(y);
                return y;
            } else {
                rotate(x);
                rotate(x);
                return x;
            }
        }
    } // ----------- end of nested BalanceableBinaryTree class -----------

    protected int getAux(Position<Entry<K, V>> p) {
        return tree.getAux(p);
    }

    protected void setAux(Position<Entry<K, V>> p, int value) {
        tree.setAux(p, value);
    }

    // We reuse the LinkedBinaryTree class. A limitation here is that we only use
    // the key.
    // protected LinkedBinaryTree<Entry<K, V>> tree = new LinkedBinaryTree<Entry<K,
    // V>>();
    protected BalanceableBinaryTree<K, V> tree = new BalanceableBinaryTree<>();

    /**
     * Constructs an empty map using the natural ordering of keys.
     */
    public TreeMap() {
        super(); // the AbstractSortedMap constructor
        tree.addRoot(null); // create a sentinel leaf as root
    }

    /**
     * Constructs an empty map using the given comparator to order keys.
     *
     * @param comp comparator defining the order of keys in the map
     */
    public TreeMap(Comparator<K> comp) {
        super(comp); // the AbstractSortedMap constructor
        tree.addRoot(null); // create a sentinel leaf as root
    }

    /**
     * Returns the number of entries in the map.
     *
     * @return number of entries in the map
     */
    @Override
    public int size() {
        return (tree.size() - 1) / 2; // only internal nodes have entries
    }

    protected Position<Entry<K, V>> restructure(Position<Entry<K, V>> x) {
        return tree.restructure(x);
    }

    /**
     * Rebalances the tree after an insertion of specified position. This version of
     * the method does not do anything, but it can be overridden by subclasses.
     *
     * @param p the position which was recently inserted
     */
    protected void rebalanceInsert(Position<Entry<K, V>> p) {
        // LEAVE EMPTY
    }

    /**
     * Rebalances the tree after a child of specified position has been removed.
     * This version of the method does not do anything, but it can be overridden by
     * subclasses.
     *
     * @param p the position of the sibling of the removed leaf
     */
    protected void rebalanceDelete(Position<Entry<K, V>> p) {
        // LEAVE EMPTY
    }

    /**
     * Rebalances the tree after an access of specified position. This version of
     * the method does not do anything, but it can be overridden by a subclasses.
     *
     * @param p the Position which was recently accessed (possibly a leaf)
     */
    protected void rebalanceAccess(Position<Entry<K, V>> p) {
        // LEAVE EMPTY
    }

    /**
     * Utility used when inserting a new entry at a leaf of the tree
     */
    private void expandExternal(Position<Entry<K, V>> p, Entry<K, V> entry) {
        tree.set(p, entry);
        tree.addLeft(p, null);
        tree.addRight(p, null);
    }

    // Some notational shorthands for brevity (yet not efficiency)
    protected Position<Entry<K, V>> root() {
        return tree.root();
    }

    protected Position<Entry<K, V>> parent(Position<Entry<K, V>> p) {
        return tree.parent(p);
    }

    protected Position<Entry<K, V>> left(Position<Entry<K, V>> p) {
        return tree.left(p);
    }

    protected Position<Entry<K, V>> right(Position<Entry<K, V>> p) {
        return tree.right(p);
    }

    protected Position<Entry<K, V>> sibling(Position<Entry<K, V>> p) {
        return tree.sibling(p);
    }

    protected boolean isRoot(Position<Entry<K, V>> p) {
        return tree.isRoot(p);
    }

    protected boolean isExternal(Position<Entry<K, V>> p) {
        return tree.isExternal(p);
    }

    protected boolean isInternal(Position<Entry<K, V>> p) {
        return tree.isInternal(p);
    }

    protected void set(Position<Entry<K, V>> p, Entry<K, V> e) {
        tree.set(p, e);
    }

    protected Entry<K, V> remove(Position<Entry<K, V>> p) {
        return tree.remove(p);
    }

    /**
     * Returns the position in p's subtree having the given key (or else the
     * terminal leaf).
     *
     * @param key a target key
     * @param p   a position of the tree serving as root of a subtree
     * @return Position holding key, or last node reached during search
     */
    private Position<Entry<K, V>> treeSearch(Position<Entry<K, V>> p, K key) {
        if (isExternal(p)) return p; // ran off the tree - return the sentinel leaf

        int comp = compare(key, p.getElement().getKey());

        if (comp == 0) {
            return p;
        } else if (comp < 0) {
            return treeSearch(left(p), key);
        } else {
            return treeSearch(right(p), key);
        }
    }

    /**
     * Returns position with the minimal key in the subtree rooted at Position p.
     *
     * @param p a Position of the tree serving as root of a subtree
     * @return Position with minimal key in subtree
     */
    protected Position<Entry<K, V>> treeMin(Position<Entry<K, V>> p) {
        Position<Entry<K, V>> curr = p;
        while (isInternal(left(curr))) curr = left(curr);
        return curr;
    }

    /**
     * Returns the position with the maximum key in the subtree rooted at p.
     *
     * @param p a Position of the tree serving as root of a subtree
     * @return Position with maximum key in subtree
     */
    protected Position<Entry<K, V>> treeMax(Position<Entry<K, V>> p) {
        Position<Entry<K, V>> curr = p;
        while (isInternal(right(curr))) curr = right(curr);
        return curr;
    }

    /**
     * Returns the value associated with the specified key, or null if no such entry
     * exists.
     *
     * @param key the key whose associated value is to be returned
     * @return the associated value, or null if no such entry exists
     */
    @Override
    public V get(K key) throws IllegalArgumentException {
        if (isEmpty()) return null;
        Position<Entry<K, V>> p = treeSearch(root(), key);
        if (isExternal(p)) return null; // not found
        rebalanceAccess(p);
        return p.getElement().getValue();
    }

    /**
     * Associates the given value with the given key. If an entry with the key was
     * already in the map, this replaced the previous value with the new one and
     * returns the old value. Otherwise, a new entry is added and null is returned.
     *
     * @param key   key with which the specified value is to be associated
     * @param value value to be associated with the specified key
     * @return the previous value associated with the key (or null, if no such
     * entry)
     */
    @Override
    public V put(K key, V value) throws IllegalArgumentException {
        Entry<K, V> entry = new MapEntry<>(key, value);
        if (isEmpty()) {
            expandExternal(root(), entry);
            rebalanceInsert(root());
            return null;
        }
        Position<Entry<K, V>> p = treeSearch(root(), key);
        if (isInternal(p)) {
            // key exists - update
            V old = p.getElement().getValue();
            set(p, entry);
            rebalanceAccess(p);
            return old;
        }
        // p is an external sentinel - expand it
        expandExternal(p, entry);
        rebalanceInsert(p);
        return null;
    }


    /**
     * Removes the entry with the specified key, if present, and returns its
     * associated value. Otherwise does nothing and returns null.
     *
     * @param key the key whose entry is to be removed from the map
     * @return the previous value associated with the removed key, or null if no
     * such entry exists
     */
    @Override
    public V remove(K key) throws IllegalArgumentException {
        if (isEmpty()) return null;

        Position<Entry<K, V>> p = treeSearch(root(), key);

        // treeSearch returns external sentinel if key not found
        if (isExternal(p)) {
            rebalanceAccess(p);
            return null;
        }

        V oldValue = p.getElement().getValue();

        // Case: two internal children - replace with inorder successor
        if (isInternal(left(p)) && isInternal(right(p))) {
            Position<Entry<K, V>> successor = treeMin(right(p));
            set(p, successor.getElement());
            p = successor;
        }

        // Now p has at most one internal child
        Position<Entry<K, V>> leaf = isExternal(left(p)) ? left(p) : right(p);
        Position<Entry<K, V>> sib = sibling(leaf);

        remove(leaf);
        remove(p);

        rebalanceDelete(sib);
        return oldValue;
    }

    // additional behaviors of the SortedMap interface

    /**
     * Returns the entry having the least key (or null if map is empty).
     *
     * @return entry with least key (or null if map is empty)
     */
    @Override
    public Entry<K, V> firstEntry() {
        if (isEmpty())
            return null;
        return treeMin(root()).getElement();
    }

    /**
     * Returns the entry having the greatest key (or null if map is empty).
     *
     * @return entry with greatest key (or null if map is empty)
     */
    @Override
    public Entry<K, V> lastEntry() {
        if (isEmpty())
            return null;
        return treeMax(root()).getElement();
    }

    /**
     * Returns the entry with least key greater than or equal to given key (or null
     * if no such key exists).
     *
     * @return entry with least key greater than or equal to given (or null if no
     * such entry)
     * @throws IllegalArgumentException if the key is not compatible with the map
     */
    @Override
    public Entry<K, V> ceilingEntry(K key) throws IllegalArgumentException {
        Position<Entry<K, V>> p = root();
        Entry<K, V> candidate = null;

        while (isInternal(p)) {
            int comp = compare(key, p.getElement().getKey());

            if (comp == 0) {
                return p.getElement();
            } else if (comp < 0) {
                // current key > target → possible ceiling
                candidate = p.getElement();
                p = left(p);
            } else {
                // current key < target → go right
                p = right(p);
            }
        }

        return candidate;
    }

    /**
     * Returns the entry with greatest key less than or equal to given key (or null
     * if no such key exists).
     *
     * @return entry with greatest key less than or equal to given (or null if no
     * such entry)
     * @throws IllegalArgumentException if the key is not compatible with the map
     */
    @Override
    public Entry<K, V> floorEntry(K key) throws IllegalArgumentException {
        Position<Entry<K, V>> p = root();
        Entry<K, V> candidate = null;

        while (isInternal(p)) {
            int comp = compare(key, p.getElement().getKey());

            if (comp == 0) {
                return p.getElement();
            } else if (comp < 0) {
                // current key > target → go left
                p = left(p);
            } else {
                // current key < target → possible floor
                candidate = p.getElement();
                p = right(p);
            }
        }

        return candidate;
    }

    /**
     * Returns the entry with greatest key strictly less than given key (or null if
     * no such key exists).
     *
     * @return entry with greatest key strictly less than given (or null if no such
     * entry)
     * @throws IllegalArgumentException if the key is not compatible with the map
     */
    @Override
    public Entry<K, V> lowerEntry(K key) throws IllegalArgumentException {
        Position<Entry<K, V>> p = root();
        Entry<K, V> candidate = null;

        while (isInternal(p)) {
            int comp = compare(key, p.getElement().getKey());

            if (comp == 0) {
                return candidate;
            } else if (comp < 0) {
                // current key > target → go left
                p = left(p);
            } else {
                // current key < target → possible floor
                candidate = p.getElement();
                p = right(p);
            }
        }

        return candidate;
    }

    /**
     * Returns the entry with least key strictly greater than given key (or null if
     * no such key exists).
     *
     * @return entry with least key strictly greater than given (or null if no such
     * entry)
     * @throws IllegalArgumentException if the key is not compatible with the map
     */
    @Override
    public Entry<K, V> higherEntry(K key) throws IllegalArgumentException {
        Position<Entry<K, V>> p = root();
        Entry<K, V> candidate = null;

        while (isInternal(p)) {
            int comp = compare(key, p.getElement().getKey());

            if (comp == 0) {
                return p.getElement();
            } else if (comp < 0) {
                // current key > target → possible ceiling
                candidate = p.getElement();
                p = left(p);
            } else {
                // current key < target → go right
                p = right(p);
            }
        }

        return candidate;
    }

    // Support for iteration

    /**
     * Returns an iterable collection of all key-value entries of the map.
     *
     * @return iterable collection of the map's entries
     */
    @Override
    public Iterable<Entry<K, V>> entrySet() {
        ArrayList<Entry<K, V>> buffer = new ArrayList<>(size());
        for (Position<Entry<K, V>> p : tree.inorder()) {
            if (isInternal(p)) {
                buffer.add(p.getElement());
            }
        }
        return buffer;
    }

    public String toString() {
        return tree.toString();
    }

    /**
     * Returns an iterable containing all entries with keys in the range from
     * <code>fromKey</code> inclusive to <code>toKey</code> exclusive.
     *
     * @return iterable with keys in desired range
     * @throws IllegalArgumentException if <code>fromKey</code> or
     *                                  <code>toKey</code> is not compatible with
     *                                  the map
     */
    @Override
    public Iterable<Entry<K, V>> subMap(K fromKey, K toKey) throws IllegalArgumentException {
        ArrayList<Entry<K, V>> output = new ArrayList<>(size());
        Iterator<Entry<K, V>> inorder = entrySet().iterator();
        while (inorder.hasNext()) {
            Entry<K, V> curr = inorder.next();
            if (compare(curr.getKey(), fromKey) >= 0) { // start adding to output
                output.addLast(curr);
                while (inorder.hasNext()) {
                    curr = inorder.next();
                    if (compare(curr.getKey(), toKey) > 0) { // if past toKey stop
                        return output;
                    }
                    output.addLast(curr);
                }
            }
        }
        return output;
    }

    protected void rotate(Position<Entry<K, V>> p) {
        tree.rotate(p);
    }

    // remainder of class is for debug purposes only

    /**
     * Prints textual representation of tree structure (for debug purpose only).
     */
    protected void dump() {
        dumpRecurse(root(), 0);
    }

    /**
     * This exists for debugging only
     */
    private void dumpRecurse(Position<Entry<K, V>> p, int depth) {
        String indent = (depth == 0 ? "" : String.format("%" + (2 * depth) + "s", ""));
        if (isExternal(p))
            System.out.println(indent + "leaf");
        else {
            System.out.println(indent + p.getElement());
            dumpRecurse(left(p), depth + 1);
            dumpRecurse(right(p), depth + 1);
        }
    }

    public String toBinaryTreeString() {
        BinaryTreePrinter<Entry<K, V>> btp = new BinaryTreePrinter<>(this.tree);
        return btp.print();
    }

    public static void main(String[] args) {
        TreeMap<Integer, Integer> treeMap = new TreeMap<Integer, Integer>();

        Random rnd = new Random();
        int n_max = 50;
        int n = 100;
        // rnd.ints(1, n_max).limit(n).distinct().boxed().forEach(x -> treeMap.put(x,
        // x));

        Consumer<Integer> modify = x -> {
            if (rnd.nextFloat() > 0.5)
                treeMap.put(x, 0);
            else
                treeMap.remove(x);
        };
        BinaryTreePrinter<Entry<Integer, Integer>> btp = new BinaryTreePrinter<>(treeMap.tree);
        System.out.println(btp.print());

        rnd.ints(1, n_max).limit(10000000).boxed().forEach(modify);
        System.out.println(btp.print());

        AVLTreeMap<Integer, Integer> avl = new AVLTreeMap<Integer, Integer>();
        for (Position<Entry<Integer, Integer>> i : treeMap.tree.inorder()) {
            if (i.getElement() != null) {
                avl.put(i.getElement().getKey(), 0);
            }
        }
        System.out.println(avl.toBinaryTreeString());
    }
}
