import java.util.Comparator;
import java.util.Iterator;

public class BinarySearchTree<E> extends AbstractBinaryTree<E> {
    private Comparator<? super Object> comparator;

    public BinarySearchTree() {
        super();
    }

    /**
     * Uses the find(E data) method to determine whether or not a not with the given data exists.
     * If the find method returns a Node then a node with the given data exists, else one does not exists.
     * @param data The data to find in the tree.
     * @return A boolean if a Node with the given data exists in the tree.
     */
    public boolean contains(Object data) {
        return this.findNode((E) data) != null;
    }

    /**
     * Given some data, will traverse the tree and look for the Node that contains that data.
     * @param data The data to find in the tree.
     * @return A Node if a node containing the given data does exist, otherwise null.
     */
    private Node<E> findNode(E data) {
        Node<E> temp = null;
        Node<E> current = super.getRoot();

        // No Nodes exist in the tree, no need to search
        if (current == null) {return null;}

        while(temp == null) {
            int comparison = compare(data, current.getData());

            if(comparison < 0) { // data is less than current node data
                Node<E> left = current.getLeft();
                if(left != null) {
                    current = left;
                }
            } else if(comparison > 0) { // data is greater than current node data
                Node<E> right = current.getRight();
                if(right != null) {
                    current = right;
                }
            } else { // Elements are equal, so node exists in tree.
                temp = current;
            }
        }
        return temp;
    }

    /**
     * Taking two elements of generic type will compare the two, if a comparator has not been set it is assumed that
     * the elements are comparable and will be compared using standard compareTo. If a comparator has been set, the
     * comparator will be used to compare the two elements instead.
     * @param a The first element being compared
     * @param b The second element being compared
     * @return A int representing the comparison results.
     * 0: the elements are equal
     * 1: element a is greater than element b
     * -1: element a is less that element b
     */
    @Override
    public int compare(Object a, Object b) {
        if (a instanceof Comparable && b instanceof Comparable && this.comparator == null) {
            return ((Comparable) a).compareTo(b); // Unchecked
        } else {
            return this.comparator.compare(a,b);
        }
    }

    public Iterator<E> iterator() {
        return null;
    }

    @Override
    public boolean isPersistent() {return false;}
}
