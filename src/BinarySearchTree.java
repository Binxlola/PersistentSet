import java.util.Iterator;

public class BinarySearchTree<E> extends AbstractBinaryTree<E> {

    public BinarySearchTree() {super();}

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

    public Iterator<E> iterator() {
        return null;
    }

    @Override
    public boolean isPersistent() {return false;}

    /**
     * Just here to meet extends requirements, logic not needed here.
     */
    @Override
    public void persist(Node<E> persistNode, Node<E> modifyNode, String position, int level, String action, boolean modify){}

    @Override
    public int getNumVersions() { return 0;}
}
