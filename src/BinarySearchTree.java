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

    public boolean add(E data) {
        //TODO needs to be completed
        Node<E> temp = new Node<>(data);
        boolean isAdded = false;

        if(this.root == null) {this.root = temp;}
        else {
            Node<E> current = this.root;

            while(!isAdded) {
                int comparison = this.compare(data, current.getData());

                if(comparison < 0) {

                } else if(comparison > 0) {

                } else { // Elements are equal, no duplicates are allowed
                    isAdded = true;
                }
            }
        }
        if(isAdded) {this.size++;}
        return isAdded;
    }

    @Override
    public boolean contains(Object data) {
        //TODO complete this method using an algorithm then is better than O(n)
        return false;
    }

    @Override
    public boolean remove(Object data) {
        //TODO complete this method to remove an element from the tree if it exists
        return false;
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
