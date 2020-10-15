import java.util.ArrayList;

/**
 * An implementation of a binary search tree which extends the functionality of AbstractBinaryTree parent class.
 * This version of a binary search tree is persistent. Such that everytime and add or remove action is made on the tree
 * a new version of the tree will be created and set as the main version whilst the previous version that does not contain
 * the change will be added to an array that holds all versions of the tree.
 * When creating a new version any node that has been traversed will be "cloned" such that there will be two identical objects
 * other than the fact that they are not the same objects. Then links to any un-traversed nodes will be made. This links will be
 * to original node and not cloned nodes.
 * @param <E> The data type that is being used with the tree, such that this implementation can worth with a variation of data types.
 * @author Jason Smit
 */
public class DynamicBinaryTree<E> extends AbstractBinaryTree<E> {

    ArrayList<Node<E>> versions = new ArrayList<>();
    int numVersions = 0;
    Node<E> lastEdited = null;

    public DynamicBinaryTree() {
        super();
    }

    /**
     * Uses the same add logic as AbstractBinaryTree except for when adding a root node.
     * When adding a root not it will get added directly here in this overridden method
     * @param data The data to be added to the tree
     * @return A boolean representing if the node was added successfully or not
     */
    @Override
    public boolean add(E data) {
        // Shortcut the parent and just directly add the root to the tree
        if(super.getRoot() == null) {
            numVersions++;
            super.setRoot(new Node<>(data));
            super.getRoot().setVersion(numVersions);
            return true;
        } else {
            return super.add(data);
        }
    }

    /**
     * An implementation of a hook method described in the parent class AbstractBinaryTree.
     * Used to signal that the class is a persistent implementation and as such requires extra logic.
     * @return True as this implementation is persistent
     */
    @Override
    public boolean isPersistent() {return true;}


    /**
     * An implementation of a hook method described in the parent class AbstractBinaryTree.
     * Describes the persistent logic that makes this implementation persistent. When a node is added or removed, and new version
     * of the tree will be created and copies of any traversed nodes made, with links to any un-traversed nodes.
     * @param persistNode The node that needs to be cloned and "persisted"
     * @param modifyNode The node that is being added or removed
     * @param position The direction of traversal and or modification to the persistNode
     * @param level The level in which the traversal of the tree is currently at
     * @param action The modification type that is being performed, either add or remove
     * @param modify A boolean stating if the modification is to take place in the current call of the method
     */
    @Override
    public void persist(Node<E> persistNode, Node<E> modifyNode, String position, int level, String action, boolean modify) {
        Node<E> clone = new Node<>(persistNode.getData(), persistNode.getLeft(), persistNode.getRight());

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

        // When removing a node when we reach the end with the node for removal we leave last edited the same
        lastEdited = (action.equals("remove") && modify) ? lastEdited : clone;
        lastEdited.setVersion(numVersions);

        // At location where node needs to be added
        // Decide which side modifyNode will be set on parent
        if(modify && lastEdited != null) {
            if (position.equals("left")) {
                lastEdited.setLeft(modifyNode);
            } else if (position.equals("right")) {
                lastEdited.setRight(modifyNode);
            }
        }

        // Make sure new node version is up to date
        // Possible modifyNode is null when removing a leaf node (if so nothing happens)
        if(modifyNode != null && modifyNode.getVersion() != numVersions && modify) {
            modifyNode.setVersion(numVersions);
        }
    }

    /**
     * @return The number of versions currently held for this tree
     */
    public int getNumVersions() {return this.numVersions;}
}
