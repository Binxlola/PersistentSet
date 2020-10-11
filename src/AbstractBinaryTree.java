public abstract class AbstractBinaryTree<E> {

    private int size;
    public int size() {
        return this.size;
    }

    private Node<E> root;
    public Node<E> getRoot() {
        return root;
    }

    abstract int compare(E a, E b);

    /**
     * Given some data of generic type, will create a new node with said data. Then traverse the tree a look for a spot
     * to add the new node. If there already exists a node with the given data, the new node will not be added.
     * @param data The data to be added to the tree
     * @return A boolean representing whether or not the new node was in fact added to the tree
     */
    public boolean add(E data) {
        Node<E> temp = new Node<>(data);
        boolean isAdded = false;
        boolean isPersistent = isPersistent();

        // No root Node exists, so we set this temp Node as the root
        if(this.root == null) {
            this.root = temp;
            if(isPersistent) {
                persist(temp, null, "root");
            }
            isAdded = true;
        }
        else {
            Node<E> current = this.root;
            Node<E> previous = null;

            while(!isAdded) { // Traverse the tree looking where to add new node.
                assert current != null;
                int comparison = compare(data, current.getData());

                if(comparison < 0) { // data is less than current node data
                    if(current.getLeft() == null && !isPersistent) { // Can add new node
                        current.setLeft(temp);
                        isAdded = true;
                    }
                    else if (current.getLeft() == null && isPersistent) {
                        persist(current, previous, "left");
                    }
                    else { // Keep traversing
                        previous = current;
                        current = current.getLeft();
                    }

                } else if(comparison > 0) { // data is greater than current node data
                    if(current.getRight() == null && !isPersistent) { // Can add new node
                        current.setRight(temp);
                        isAdded = true;
                    } else if(current.getRight() == null && isPersistent) {
                        persist(current, previous, "right");
                    }
                    else  { // Keep traversing
                        previous = current;
                        current = current.getRight();
                    }

                } else { // Elements are equal, no duplicates are allowed
                    break;
                }
            }
        }
        if(isAdded && !isPersistent) {this.size++;}
        return isAdded;
    }

    /**
     * Traverse the tree comparing nodes to the given data, if while traversing a node is found to contain the given data
     * that node will be removed from the tree.
     * @param data The data to be removed from the tree
     * @return A boolean stating whether or not a node containing the given data was in fact removed from the tree
     */
    public boolean remove(Object data) {
        //TODO Refine logic for node removal
        Node<E> previous = null;
        Node<E> current = this.root;

        boolean isRemoved = false;
        boolean isLeft = false;
        boolean isRight = false;


        // An initial check to see if the item to be removed is the root
        if(compare((E) data, current.getData()) == 0) {
            this.root = this.findNewRoot(this.root);
            this.size--;
            isRemoved = true;

        }

        while(!isRemoved) { // If current is null, the end of the tree as been reached and the node does not exist
            int comparison = compare((E) data, current.getData());

            if(comparison < 0) { // data is less than current node data
                Node<E> left = current.getLeft();
                if(left != null) {
                    previous = current;
                    current = left;

                    // Keep track of direction of traverse
                    isLeft = true;
                    isRight = false;
                }
            } else if(comparison > 0) { // data is greater than current node data
                Node<E> right = current.getRight();
                if(right != null) {
                    previous = current;
                    current = right;

                    // Keep track of direction of traverse
                    isRight = true;
                    isLeft = false;
                }
            } else { // Elements are equal, this is the node to be removed.
                if(isLeft) { previous.setLeft(null);}
                else if(isRight) {previous.setRight(null);}
                isRemoved = true;
                this.size --;
            }
        }

        return isRemoved;
    }

    private Node<E> findNewRoot(Node<E> toBeRemoved) {
        Node<E> replacementNode = null;

        // Node being removed only has one child node
        if (toBeRemoved.getLeft() != null && toBeRemoved.getRight() == null) {replacementNode = toBeRemoved.getLeft();}
        else if (toBeRemoved.getLeft() == null && toBeRemoved.getRight()!= null) {replacementNode = toBeRemoved.getRight();}

        // Node being removed has two child nodes
        else if (toBeRemoved.getLeft() != null && toBeRemoved.getRight() != null) {
            // find the inorder successor and use it as the replacement Node
            Node<E> parentNode;
            replacementNode = toBeRemoved.getRight();

            // Make left child of replacement the left of Node being removed
            if (replacementNode.getLeft() == null) { replacementNode.setLeft(toBeRemoved.getLeft());}
            else {  //find left-most descendant of right subtree of removalNode
                do {
                    parentNode = replacementNode;
                    replacementNode = replacementNode.getLeft();
                }
                while (replacementNode.getLeft() != null);

                // move the right child of replacementNode to be the left
                // child of the parent of replacementNode
                parentNode.setLeft(replacementNode.getRight());
                // move the children of removalNode to be children of
                // replacementNode
                replacementNode.setLeft(toBeRemoved.getLeft());
                replacementNode.setRight(toBeRemoved.getRight());
            }
        }
        // else both leftChild and rightChild null so no replacementNode
        return replacementNode;
    }

    abstract boolean isPersistent();
    abstract void persist(Node<E> currentNode, Node<E>  previousNode, String position);
}
