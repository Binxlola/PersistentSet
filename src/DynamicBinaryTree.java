import java.util.ArrayList;

public class DynamicBinaryTree<E> extends AbstractBinaryTree<E> {

    BinarySearchTree<E> currentVersion = null;
    ArrayList<BinarySearchTree<E>> versions = new ArrayList<BinarySearchTree<E>>();
    int numVersions = 0;

    public DynamicBinaryTree() {

    }

    @Override
    public boolean add(E data) {
        currentVersion.add(data);
        return true;
    }

    @Override
    int compare(E a, E b) {
        return 0;
    }

    @Override
    public boolean isPersistent() {return true;}

    @Override
    public void persist(Node<E> current, Node<E> previous, String position) {
        // Starting new version
        if(position.equals("root")) {
            currentVersion = new BinarySearchTree<>();
            Node<E> clone = new Node<E>(current.getData());
        }
    }
}
