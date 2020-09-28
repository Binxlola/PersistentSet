import java.util.AbstractSet;
import java.util.Comparator;
import java.util.Iterator;

public class BinarySearchTree<E> extends AbstractSet<E> {

    private int size;
    private Node<E> root;
    private Comparator<? super E> comparator;

    public BinarySearchTree() {
        super();
        this.size = 0;
        this.root = null;
    }

    /**
     * Given some data of generic type, will create a new node with said data. Then traverse the tree a look for a spot
     * to add the new node. If there already exists a node with the given data, the new node will not be added.
     * @param data The data to be added to the tree
     * @return A boolean representing whether or not the new node was in fact added to the tree
     */
    public boolean add(E data) {
        Node<E> temp = new Node<>(data);
        boolean isAdded = false;

        if(this.root == null) {this.root = temp;}
        else {
            Node<E> current = this.root;

            while(!isAdded) { // Traverse the tree looking where to add new node.
                int comparison = this.compare(data, current.getData());

                if(comparison < 0) { // data is less than current node data
                    if(current.getLeft() == null) { // Can add new node
                        current.setLeft(temp);
                        isAdded = true;
                    } else { // Keep traversing
                        current = current.getLeft();
                    }

                } else if(comparison > 0) { // data is greater than current node data
                    if(current.getRight() == null) { // Can add new node
                        current.setRight(temp);
                        isAdded = true;
                    } else { // Keep traversing
                        current = current.getRight();
                    }

                } else { // Elements are equal, no duplicates are allowed
                    break;
                }
            }
        }
        if(isAdded) {this.size++;}
        return isAdded;
    }

    /**
     * Uses the find(E data) method to determine whether or not a not with the given data exists.
     * If the find method returns a Node then a node with the given data exists, else one does not exists.
     * @param data The data to find in the tree.
     * @return A boolean if a Node with the given data exists in the tree.
     */
    @Override
    public boolean contains(Object data) {
        return this.findNode((E) data) != null;
    }

    @Override
    public boolean remove(Object data) {
        //TODO complete this method to remove an element from the tree if it exists
        return false;
    }

    /**
     * Given some data, will traverse the tree and look for the Node that contains that data.
     * @param data The data to find in the tree.
     * @return A Node if a node containing the given data does exist, otherwise null.
     */
    private Node<E> findNode(E data) {
        Node<E> temp = null;
        Node<E> current = this.root;

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
    private int compare(E a, E b) {
        //TODO Fill out the compare method so that it may be used when adding elements.
        return 0;
    }

    @Override
    public Iterator<E> iterator() {
        return null;
    }

    @Override
    public int size() {
        return this.size;
    }
}
