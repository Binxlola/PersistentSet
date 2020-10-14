import java.util.ArrayList;

public class DynamicBinaryTree<E> extends AbstractBinaryTree<E> {

    ArrayList<Node<E>> versions = new ArrayList<Node<E>>();
    int numVersions = 0;
    Node<E> lastEdited = null;

    public DynamicBinaryTree() {
        super();
    }

    @Override
    public boolean add(E data) {
        // Short cut the parent and just directly add the root to the tree
        if(super.getRoot() == null) {
            numVersions++;
            super.setRoot(new Node<E>(data));
            super.getRoot().setVersion(numVersions);
            return true;
        } else {
            return super.add(data);
        }
    }

    @Override
    public boolean isPersistent() {return true;}

    @Override
    public void persist(Node<E> persistNode, Node<E> modifyNode, String position, int level, String action, boolean modify) {
        Node<E> clone = new Node<E>(persistNode.getData(), persistNode.getLeft(), persistNode.getRight());

        // Currently at the root of the current/old version. New version needs to get created
        if (level == 1) {
            versions.add(super.getRoot());
            super.setRoot(clone);
            numVersions++;
        } else if (level > 1) {
            // Traversing Nodes
            Node<E> lastLeft = lastEdited.getLeft();
            Node<E> lastRight = lastEdited.getRight();
            E currentData = persistNode.getData();
            if (lastLeft != null && currentData.equals(lastLeft.getData())) {
                lastEdited.setLeft(clone);
            } else if (lastRight != null && currentData.equals(lastRight.getData())) {
                lastEdited.setRight(clone);
            }
        }
        lastEdited = clone;
        lastEdited.setVersion(numVersions);

        // At location where node needs to be added
        if(modify) {
            if (position.equals("left")) {
                lastEdited.setLeft(action.equals("add") ? modifyNode : null);
            } else if (position.equals("right")) {
                lastEdited.setRight(action.equals("add") ? modifyNode : null);
            }
        }

        // Make sure new node version is up to date
        if(modifyNode.getVersion() != numVersions) {modifyNode.setVersion(numVersions);}
    }

    public int getNumVersions() {return this.numVersions;}
}
