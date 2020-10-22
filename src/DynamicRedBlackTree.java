import java.util.ArrayList;

public class DynamicRedBlackTree<E> extends AbstractBinaryTree<E> {

    ArrayList<Node<E>> versions = new ArrayList<>();
    ArrayList<Node<E>> editedNodes = new ArrayList<>();
    int numVersions = 0;
    Node<E> lastEdited = null;

    public DynamicRedBlackTree() {
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
        // Set color of node to black as it is the root node
        if(super.getRoot() == null) {
            numVersions++;

//            Node<E> root = new Node<>(data);
//            root.setRed(false);
//            root.setVersion(numVersions);
//            super.setRoot(root);

            super.setRoot(test());

            return true;
        } else { // Not root node, continue normal add logic
            return super.add(data);
        }
    }


    @Override
    boolean isPersistent() {
        return true;
    }

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
        Node<E> clone = new Node<>(persistNode.getData(), persistNode.getLeft(), persistNode.getRight(),persistNode.isRed());

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
        // Edited nodes are added to array for re-balancing
        lastEdited = (action.equals("remove") && modify) ? lastEdited : clone;
        lastEdited.setVersion(numVersions);
        editedNodes.add(clone);

        // At location where node needs to be added
        // Decide which side modifyNode will be set on parent
        if(modify && lastEdited != null) {
            if (position.equals("left")) {
                lastEdited.setLeft(modifyNode);
            } else if (position.equals("right")) {
                lastEdited.setRight(modifyNode);
            }
            modifyNode.setRed(true);


            // #TODO is this condition correct?
            if(editedNodes.size() > 1) {
                this.balance(modifyNode);
            }

            this.editedNodes.clear();
        }

        // Make sure new node version is up to date
        // Possible modifyNode is null when removing a leaf node (if so nothing happens)
        // Adding a node will make it a leaf and therefore has to be red for re-balancing
        if(modifyNode != null && modifyNode.getVersion() != numVersions && modify) {
            modifyNode.setVersion(numVersions);
        }
    }

    private void balance(Node<E> modifyNode) {
        // move back 2 steps as last edited node will be parent.
        Node<E> grandparent = editedNodes.get(editedNodes.size() - 2);
        Node<E> parent = editedNodes.get(editedNodes.size() - 1);
        Node<E> uncle;
        boolean uncleIsRed= false;

        while(parent.isRed()) {
            if(grandparent.getLeft() != null && grandparent.getLeftData().equals(parent.getData())) { // Uncle is on right side
                uncle = grandparent.getRight();
                uncleIsRed = uncle != null && uncle.isRed();
                if (this.reColour(grandparent, parent, uncle)) {
                    modifyNode = grandparent;

                    if(grandparent != super.getRoot()) { // Only shift when grandparent is not the root
                        parent = editedNodes.get(editedNodes.indexOf(grandparent) - 1);
                        grandparent = editedNodes.get(editedNodes.indexOf(grandparent) - 2);
                        uncle = grandparent.getLeft();
                    }
                } else if(!uncleIsRed) {
                    if(modifyNode == parent.getRight()) {
                        modifyNode = parent;
                        parent = editedNodes.get(editedNodes.indexOf(parent) - 1);

                        if(parent != super.getRoot()) { // Only shift when parent is not the root
                            grandparent = editedNodes.get(editedNodes.indexOf(parent) - 1);
                        }

                        uncle = grandparent.getLeft();
                        rotateLeft(modifyNode, parent);
                        parent = grandparent.getRight();
                    }

                    parent.setRed(false);
                    grandparent.setRed(true);
                    rotateRight(grandparent, modifyNode);
                }
            } else { // Uncle is on the left
                uncle = grandparent.getLeft();
                uncleIsRed = uncle != null && uncle.isRed();
                if(this.reColour(grandparent, parent, uncle)) {
                    modifyNode = grandparent;

                    if(grandparent != super.getRoot()) { // Only shift when grandparent is not the root
                        parent = editedNodes.get(editedNodes.indexOf(grandparent) - 1);
                        grandparent = editedNodes.get(editedNodes.indexOf(grandparent) - 1);
                        uncle = grandparent.getRight();
                    }

                } else if(!uncleIsRed) {
                    if(modifyNode == parent.getLeft()) {
                        modifyNode = parent;
                        parent = editedNodes.get(editedNodes.indexOf(parent) - 1);

                        if(parent != super.getRoot()) { // Only shift when parent is not the root
                            grandparent = editedNodes.get(editedNodes.indexOf(parent) - 1);
                        }

                        uncle = grandparent.getRight();
                        rotateRight(modifyNode, parent);
                        parent = grandparent.getLeft();
                    }

                    parent.setRed(false);
                    grandparent.setRed(true);
                    rotateLeft(grandparent, modifyNode);
                }
            }
        }
        super.getRoot().setRed(false);
    }

    public void rotateRight(Node<E> rotationNode, Node<E> parent){
        Node<E> leftChild = rotationNode.getLeft();
        Node<E> leftChildRightChild = leftChild.getRight();
        if (!parent.isRed()) {
            parent.setRight(leftChild);
        }
        rotationNode.setLeft(leftChildRightChild);
        leftChild.setRight(rotationNode);

        connectRotation(parent, rotationNode, leftChild);

    }

    public void rotateLeft(Node<E> rotationNode, Node<E> parent) {
        Node<E> rightChild = rotationNode.getRight();
        Node<E> rightChildLeftChild = rightChild.getLeft();
        if(!parent.isRed()) {
            parent.setLeft(rightChild);
        }
        rotationNode.setRight(rightChildLeftChild);
        rightChild.setLeft(rotationNode);

        connectRotation(parent, rotationNode, rightChild);
    }

    public void connectRotation(Node<E> parent, Node<E> rotationNode, Node<E> newChild) {
        if(rotationNode == super.getRoot()) {
            super.setRoot(newChild);
        } else if (parent.isRed()) {
            Node<E> rotationParent = editedNodes.get(editedNodes.indexOf(rotationNode) - 1);
            if(rotationParent.getRight() == rotationNode) {
                editedNodes.get(editedNodes.indexOf(rotationNode) - 1).setRight(newChild);
            } else {
                editedNodes.get(editedNodes.indexOf(rotationNode) - 1).setLeft(newChild);
            }
        }
    }

    private boolean reColour(Node<E> grandparent, Node<E> parent, Node<E> uncle) {
        if(uncle != null && uncle.isRed()) {
            parent.setRed(false);
            uncle.setRed(false);
            grandparent.setRed(true);
            uncle.setVersion(numVersions);
            return true;
        }

        return false;
    }

    @Override
    public int getNumVersions() {
        return 0;
    }

    public Node<E> test() {
        Node<E> first = new Node<>((E) Integer.valueOf(9), null, null, true);
        Node<E> second = new Node<>((E) Integer.valueOf(13), null, null, true);
        Node<E> third = new Node<>((E) Integer.valueOf(23), null, null, true);

        Node <E> forth = new Node<>((E) Integer.valueOf(12), first, second, false);
        Node <E> fifth = new Node<>((E) Integer.valueOf(19), null, third, false);

        Node<E> sixth = new Node<>((E) Integer.valueOf(15), forth, fifth, true);
        Node<E> seventh = new Node<>((E) Integer.valueOf(5), null, null, false);

        return new Node<>((E) Integer.valueOf(8), seventh, sixth, false);
    }
}
