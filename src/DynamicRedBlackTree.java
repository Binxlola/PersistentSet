public class DynamicRedBlackTree<E> extends AbstractBinaryTree<E> {
    @Override
    boolean isPersistent() {
        return false;
    }

    @Override
    void persist(Node<E> persistNode, Node<E> modifyNode, String position, int level, String action, boolean modify) {

    }

    @Override
    public int getNumVersions() {
        return 0;
    }
}
