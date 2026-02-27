package project20280.tree;

import project20280.interfaces.BinaryTree;
import project20280.interfaces.Position;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;


/**
 * Concrete implementation of a binary tree using a node-based, linked
 * structure.
 */
public class LinkedBinaryTree<E> extends AbstractBinaryTree<E> {

    static java.util.Random rnd = new java.util.Random();
    /**
     * The root of the binary tree
     */
    protected Node<E> root = null; // root of the tree

    // LinkedBinaryTree instance variables
    /**
     * The number of nodes in the binary tree
     */
    private int size = 0; // number of nodes in the tree

    /**
     * The middle of the longest path
     */
    private Position<E> diameterMid;

    /**
     * Constructs an empty binary tree.
     */
    public LinkedBinaryTree() {
    } // constructs an empty binary tree

    // constructor

    public static LinkedBinaryTree<Integer> makeRandom(int n) {
        LinkedBinaryTree<Integer> bt = new LinkedBinaryTree<>();
        bt.root = randomTree(null, 1, n);
        return bt;
    }

    // nonpublic utility

    public static <T extends Integer> Node<T> randomTree(Node<T> parent, Integer first, Integer last) {
        if (first > last) return null;
        else {
            Integer treeSize = last - first + 1;
            Integer leftCount = rnd.nextInt(treeSize);
            Integer rightCount = treeSize - leftCount - 1;
            Node<T> root = new Node<T>((T) ((Integer) (first + leftCount)), parent, null, null);
            root.setLeft(randomTree(root, first, first + leftCount - 1));
            root.setRight(randomTree(root, first + leftCount + 1, last));
            return root;
        }
    }

    public void construct(E[] inorder, E[] preorder) {
        root = constructHelper(inorder, preorder, null, 0, inorder.length - 1, new int[]{0});
    }

    public Node<E> constructHelper(E[] inorder, E[] preorder, Node<E> parent, int l, int h, int[] j) {
        if (l > h) return null;

        // find index of preorder[j[0]] in inorder
        int i;
        for (i = l; i <= h; i++)
            if (inorder[i].equals(preorder[j[0]])) break;

        // split inorder in two (l,i-1) (i+1,j)
        Node<E> node = createNode(preorder[j[0]++], parent, null, null);
        node.setLeft(constructHelper(inorder, preorder, node, l, i - 1, j));
        node.setRight(constructHelper(inorder, preorder, node, i + 1, h, j));

        return node;
    }

    public ArrayList<ArrayList<Position<E>>> rootToLeafPath() {
        ArrayList<ArrayList<Position<E>>> paths = new ArrayList<>();
        rootToLeafPathHelper(root(), new ArrayList<>(), paths);
        return paths;
    }

    private void rootToLeafPathHelper(Position<E> p,
                                      ArrayList<Position<E>> path,
                                      ArrayList<ArrayList<Position<E>>> paths) {
        if (p == null) return;

        path.add(p);

        // if at leaf, add to paths
        if (numChildren(p) == 0) {
            paths.add(new ArrayList<>(path));
        } else { // else continue down
            for (Position<E> child : children(p)) {
                rootToLeafPathHelper(child, path, paths);
            }
        }

        // backtrack
        path.remove(path.size() - 1);
    }


    // accessor methods (not already implemented in AbstractBinaryTree)

    public static void main(String [] args) throws IOException{

        /*
        LinkedBinaryTree<Character> btInorder = new LinkedBinaryTree<>();
        Character[] arrInorder = {'M' , 'X', 'U', 'E', 'A', 'F', 'N'};
        btInorder.createLevelOrder(arrInorder);
        ArrayList<Position<Character>> inorder = (ArrayList<Position<Character>>) btInorder.inorder();
        System.out.println("Inorder:");
        System.out.println(btInorder.toBinaryTreeString());
        System.out.println(inorder);

        LinkedBinaryTree<Character> btPreorder = new LinkedBinaryTree<>();
        Character[] arrPreorder = {'E' , 'X', 'F', 'A', 'M', 'U', 'N'};
        btPreorder.createLevelOrder(arrPreorder);
        ArrayList<Position<Character>> preorder = (ArrayList<Position<Character>>) btPreorder.preorder();
        System.out.println("Preorder:");
        System.out.println(btPreorder.toBinaryTreeString());
        System.out.println(preorder);

        LinkedBinaryTree<Character> btPostorder = new LinkedBinaryTree<>();
        Character[] arrPostorder = {'N' , 'E', 'M', 'X', 'A', 'F', 'U'};
        btPostorder.createLevelOrder(arrPostorder);
        ArrayList<Position<Character>> postorder = (ArrayList<Position<Character>>) btPostorder.postorder();
        System.out.println("Postorder:");
        System.out.println(btPostorder.toBinaryTreeString());
        System.out.println(postorder);

         */


        /*
        Integer [] inorder = new Integer[23];
        for (int i = 0; i < inorder.length; i++) {
            inorder[i] = i;
        }
        Integer [] preorder = {6,5,3,2,1,0,4,17,10,9,8,7,16,14,13,12,11,15,21,20,19,18,22};

        LinkedBinaryTree<Integer> bt = new LinkedBinaryTree<>();

        bt.construct(inorder, preorder);
        System.out.println(bt.diameter());
        bt.printDiameter();
         */


        Locale.setDefault(Locale.US); // ensures decimal dot in CSV if needed

        int trials = 100;

        try (FileWriter out = new FileWriter("avg_height.csv")) {
            out.write("n,avgHeight,stdDev,lnN,log2N\n");

            for (int n = 50; n <= 5000; n += 50) {
                double sum = 0.0;
                double sumSq = 0.0;

                for (int t = 0; t < trials; t++) {
                    LinkedBinaryTree<Integer> rt = new LinkedBinaryTree<>();
                    rt.setRoot(randomTree(null, 1, n));
                    int h = rt.height();
                    sum += h;
                    sumSq += (double) h * h;
                }

                double avg = sum / trials;
                double var = (sumSq / trials) - (avg * avg);
                double std = var > 0 ? Math.sqrt(var) : 0.0;

                double lnN = Math.log(n);
                double log2N = Math.log(n) / Math.log(2);

                out.write(String.format("%d,%.6f,%.6f,%.6f,%.6f%n", n, avg, std, lnN, log2N));
            }
        }

        System.out.println("Wrote avg_height.csv (import into Sheets/Excel)");

        /*
        Integer [] inorder = new Integer[23];
        for (int i = 0; i < inorder.length; i++) {
            inorder[i] = i;
        }
        Integer [] preorder = {6,5,3,2,1,0,4,17,10,9,8,7,16,14,13,12,11,15,21,20,19,18,22};

        LinkedBinaryTree<Integer> bt = new LinkedBinaryTree<>();

        bt.construct(inorder, preorder);

        System.out.println(bt.rootToLeafPath());

         */

    }


    /**
     * Factory function to create a new node storing element e.
     */
    protected Node<E> createNode(E e, Node<E> parent, Node<E> left, Node<E> right) {
        return new Node<E>(e, parent, left, right);
    }

    /**
     * Verifies that a Position belongs to the appropriate class, and is not one
     * that has been previously removed. Note that our current implementation does
     * not actually verify that the position belongs to this particular list
     * instance.
     *
     * @param p a Position (that should belong to this tree)
     * @return the underlying Node instance for the position
     * @throws IllegalArgumentException if an invalid position is detected
     */
    protected Node<E> validate(Position<E> p) throws IllegalArgumentException {
        if (!(p instanceof Node)) throw new IllegalArgumentException("Not valid position type");
        Node<E> node = (Node<E>) p; // safe cast
        if (node.getParent() == node) // our convention for defunct node
            throw new IllegalArgumentException("p is no longer in the tree");
        return node;
    }

    /**
     * Returns the number of nodes in the tree.
     *
     * @return number of nodes in the tree
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Returns the root Position of the tree (or null if tree is empty).
     *
     * @return root Position of the tree (or null if tree is empty)
     */
    @Override
    public Position<E> root() {
        return root;
    }

    // update methods supported by this class

    /**
     * Returns the Position of p's parent (or null if p is root).
     *
     * @param p A valid Position within the tree
     * @return Position of p's parent (or null if p is root)
     * @throws IllegalArgumentException if p is not a valid Position for this tree.
     */
    @Override
    public Position<E> parent(Position<E> p) throws IllegalArgumentException {
        return ((Node<E>) p).getParent();
    }

    /**
     * Returns the Position of p's left child (or null if no child exists).
     *
     * @param p A valid Position within the tree
     * @return the Position of the left child (or null if no child exists)
     * @throws IllegalArgumentException if p is not a valid Position for this tree
     */
    @Override
    public Position<E> left(Position<E> p) throws IllegalArgumentException {
        return ((Node<E>) p).getLeft();
    }

    /**
     * Returns the Position of p's right child (or null if no child exists).
     *
     * @param p A valid Position within the tree
     * @return the Position of the right child (or null if no child exists)
     * @throws IllegalArgumentException if p is not a valid Position for this tree
     */
    @Override
    public Position<E> right(Position<E> p) throws IllegalArgumentException {
        return ((Node<E>) p).getRight();
    }

    /**
     * Places element e at the root of an empty tree and returns its new Position.
     *
     * @param e the new element
     * @return the Position of the new element
     * @throws IllegalStateException if the tree is not empty
     */
    public Position<E> addRoot(E e) throws IllegalStateException {
        if (!isEmpty()) throw new IllegalStateException();
        root = createNode(e, null, null, null);
        size++;
        return root;
    }

    public void insert(E e) {
        // TODO
    }

    // recursively add Nodes to binary tree in proper position
    private Node<E> addRecursive(Node<E> p, E e) {
        // TODO
        return null;
    }

    /**
     * Creates a new left child of Position p storing element e and returns its
     * Position.
     *
     * @param p the Position to the left of which the new element is inserted
     * @param e the new element
     * @return the Position of the new element
     * @throws IllegalArgumentException if p is not a valid Position for this tree
     * @throws IllegalArgumentException if p already has a left child
     */
    public Position<E> addLeft(Position<E> p, E e) throws IllegalArgumentException {
        validate(p);
        Node<E> parent = (Node<E>) p;

        // check if p already has a left child
        if (parent.getLeft() != null) throw new IllegalArgumentException();

        // create child
        Node<E> child = createNode(e, parent, null, null);

        // set child as left child
        parent.setLeft(child);

        size++;
        return child;
    }

    /**
     * Creates a new right child of Position p storing element e and returns its
     * Position.
     *
     * @param p the Position to the right of which the new element is inserted
     * @param e the new element
     * @return the Position of the new element
     * @throws IllegalArgumentException if p is not a valid Position for this tree.
     * @throws IllegalArgumentException if p already has a right child
     */
    public Position<E> addRight(Position<E> p, E e) throws IllegalArgumentException {
        validate(p);
        Node<E> parent = (Node<E>) p;

        // check if p already has a right child
        if (parent.getRight() != null) throw new IllegalArgumentException();

        // create child
        Node<E> child = createNode(e, parent, null, null);

        // set child as right child
        parent.setRight(child);

        size++;
        return child;
    }

    /**
     * Replaces the element at Position p with element e and returns the replaced
     * element.
     *
     * @param p the relevant Position
     * @param e the new element
     * @return the replaced element
     * @throws IllegalArgumentException if p is not a valid Position for this tree.
     */
    public E set(Position<E> p, E e) throws IllegalArgumentException {
        validate(p);

        Node<E> node = (Node<E>) p;
        E replaced = node.getElement();
        node.setElement(e);
        return replaced;
    }

    /**
     * Attaches trees t1 and t2, respectively, as the left and right subtree of the
     * leaf Position p. As a side effect, t1 and t2 are set to empty trees.
     *
     * @param p  a leaf of the tree
     * @param t1 an independent tree whose structure becomes the left child of p
     * @param t2 an independent tree whose structure becomes the right child of p
     * @throws IllegalArgumentException if p is not a valid Position for this tree
     * @throws IllegalArgumentException if p is not a leaf
     */
    public void attach(Position<E> p, LinkedBinaryTree<E> t1, LinkedBinaryTree<E> t2) throws IllegalArgumentException {
        // potentially clear trees t1, t2
        validate(p);
        if (children(p).iterator().hasNext()) throw new IllegalArgumentException("Argument p is not a leaf");
        Node<E> node = (Node<E>) p;
        node.setLeft((Node<E>) t1.root());
        node.setRight((Node<E>) t2.root());
    }

    /**
     * Removes the node at Position p and replaces it with its child, if any.
     *
     * @param p the relevant Position
     * @return element that was removed
     * @throws IllegalArgumentException if p is not a valid Position for this tree.
     * @throws IllegalArgumentException if p has two children.
     */
    public E remove(Position<E> p) throws IllegalArgumentException {
        Node<E> node = validate(p);
        if (numChildren(node) > 1)
            throw new IllegalArgumentException("p has two children");

        Node<E> child = (node.getLeft() != null) ? node.getLeft() : node.getRight();

        if (child != null) {
            child.setParent(node.getParent());
        }

        if (node == root) {
            root = child;
        } else {
            Node<E> parent = node.getParent();
            if (parent.getLeft() == node) {
                parent.setLeft(child);
            } else {
                parent.setRight(child);
            }
        }

        size--;

        return node.getElement();
    }

    public String toString() {
        return positions().toString();
    }

    public void createLevelOrder(ArrayList<E> l) {
        root = createLevelOrderHelper(l, root, 0);
    }

    private Node<E> createLevelOrderHelper(java.util.ArrayList<E> l, Node<E> p, int i) {
        if (i < l.size()) {
            Node<E> node = createNode(l.get(i), p, null, null);
            node.setLeft(createLevelOrderHelper(l, node, 2*i + 1));
            node.setRight(createLevelOrderHelper(l, node, 2*i + 2));
            size++;
            return node;
        }
        return null;
    }

    public void createLevelOrder(E[] arr) {
        root = createLevelOrderHelper(arr, root, 0);
    }

    private Node<E> createLevelOrderHelper(E[] arr, Node<E> p, int i) {
        if (i < arr.length) {
            Node<E> node = createNode(arr[i], p, null, null);
            node.setLeft(createLevelOrderHelper(arr, node, 2*i + 1));
            node.setRight(createLevelOrderHelper(arr, node, 2*i + 2));
            size++;
            return node;
        }
        return null;
    }

    public String toBinaryTreeString() {
        BinaryTreePrinter<E> btp = new BinaryTreePrinter<>(this);
        return btp.print();
    }

    /**
     * Returns the longest path between any two nodes of the tree.
     */
    public int diameter() {
        int[] diameter = {0};
        diameterHelper(root(), diameter);
        return diameter[0];
    }

    private int diameterHelper(Position<E> p, int[] diameter) {
        if (p == null) return 0;

        int l = diameterHelper(left(p), diameter);
        int r = diameterHelper(right(p), diameter);

        diameter[0] = Math.max(diameter[0], l+r);

        return Math.max(l,r) + 1;
    }

    public void printDiameter() {
        ArrayList<Position<E>> bestPath = new ArrayList<Position<E>>();
        diameterHelper(root(), bestPath);

        reverseAfter(bestPath, diameterMid);

        for (Position<E> pos : bestPath) {
            System.out.print(pos.getElement() + " ");
        }
    }


    private ArrayList<Position<E>> diameterHelper(Position<E> p,
                                             ArrayList<Position<E>> bestPath) {

        if (p == null) {
            return new ArrayList<>();
        }

        ArrayList<Position<E>> leftPath = diameterHelper(left(p), bestPath);
        ArrayList<Position<E>> rightPath = diameterHelper(right(p), bestPath);

        // Check if path through this node is longest
        if (leftPath.size() + rightPath.size() > bestPath.size()) {
            bestPath.clear();

            ArrayList<Position<E>> temp = new ArrayList<>(leftPath);

            temp.add(p);
            temp.addAll(rightPath);

            bestPath.addAll(temp);

            diameterMid = p;
        }

        // Return longest downward path
        if (leftPath.size() > rightPath.size()) {
            leftPath.add(p);
            return leftPath;
        } else {
            rightPath.add(p);
            return rightPath;
        }
    }

    public static <E> void reverseAfter(ArrayList<E> list, E target) {
        int index = list.indexOf(target);

        if (index == -1 || index == list.size() - 1) {
            return; // element not found OR nothing after it
        }

        int left = index + 1;
        int right = list.size() - 1;

        while (left < right) {
            E temp = list.get(left);
            list.set(left, list.get(right));
            list.set(right, temp);

            left++;
            right--;
        }
    }

    public void setRoot(Node<E> r) {
        root = r;
    }


    /**
     * Nested static class for a binary tree node.
     */
    public static class Node<E> implements Position<E> {
        private E element;
        private Node<E> left, right, parent;

        public Node(E e, Node<E> p, Node<E> l, Node<E> r) {
            element = e;
            left = l;
            right = r;
            parent = p;
        }

        // accessor
        public E getElement() {
            return element;
        }

        // modifiers
        public void setElement(E e) {
            element = e;
        }

        public Node<E> getLeft() {
            return left;
        }

        public void setLeft(Node<E> n) {
            left = n;
        }

        public Node<E> getRight() {
            return right;
        }

        public void setRight(Node<E> n) {
            right = n;
        }

        public Node<E> getParent() {
            return parent;
        }

        public void setParent(Node<E> n) {
            parent = n;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            if (element == null) {
                sb.append("\u29B0");
            } else {
                sb.append(element);
            }
            return sb.toString();
        }
    }
}