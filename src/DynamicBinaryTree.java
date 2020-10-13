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
    public void persist(Node<E> current, Node<E> newNode, String position, int level, boolean toAdd) {
        Node<E> clone = new Node<E>(current.getData(), current.getLeft(), current.getRight());

        // Currently at the root of the current/old version. New version needs to get created
        if (level == 1) {
            versions.add(super.getRoot());
            super.setRoot(clone);
            numVersions++;
        } else if (level > 1 && !toAdd) {
            E lastRight = lastEdited.getRight().getData();
            E lastLeft = lastEdited.getLeft().getData();
            E currentData = current.getData();
            if (currentData.equals(lastLeft)) {
                lastEdited.setLeft(clone);
            } else if (currentData.equals(lastRight)) {
                lastEdited.setRight(clone);
            }
        }
        lastEdited = clone;
        lastEdited.setVersion(numVersions);

        if (position.equals("left") && toAdd) {
            lastEdited.setLeft(newNode);
        } else if (position.equals("right") && toAdd) {
            lastEdited.setRight(newNode);
        }

        if(newNode.getVersion() != numVersions) {newNode.setVersion(numVersions);}
    }

    public int getNumVersions() {return this.numVersions;}
}
