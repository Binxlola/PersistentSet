public class DynamicBinaryTree<E> extends AbstractBinaryTree<E> {

    BinarySearchTree<E> currentVersion = null;
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
}
