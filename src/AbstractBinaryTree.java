import java.util.Comparator;

public abstract class AbstractBinaryTree<E> {

    private int size;
    public int size() {
        return this.size;
    }

    private Node<E> root;
    private Comparator<? super Object> comparator;
    public Node<E> getRoot() {
        return root;
    }
    public void setRoot(Node<E> root) {this.root = root;}

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
    public int compare(Object a, Object b) {
        if (a instanceof Comparable && b instanceof Comparable && this.comparator == null) {
            return ((Comparable) a).compareTo(b); // Unchecked
        } else {
            return this.comparator.compare(a,b);
        }
    }

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
        String dir;
        int level = 1;

        // No root Node exists, so we set this temp Node as the root
        // A persistent tree will always have a root if it enters this method
        if(this.root == null && !isPersistent) {
            this.root = temp;
            isAdded = true;
        }
        else {
            Node<E> current = this.root;

            // Traverse the tree looking where to add new node.
            while(!isAdded) {
                assert current != null;
                int comparison = compare(data, current.getData());

                if(comparison < 0) { // data is less than current node data
                    dir = "left";
                    if(current.getLeft() == null && !isPersistent) { // Add new node (non-persistent)
                        current.setLeft(temp);
                        isAdded = true;
                    }
                    else if (current.getLeft() == null && isPersistent) { // Add new node persistent
                        persist(current, temp, dir, level, "add", true);
                        isAdded = true;
                    }
                    else { // Keep traversing
                        persist(current, temp, dir, level, "add", false);
                        current = current.getLeft();
                    }
                    level++;
                } else if(comparison > 0) { // data is greater than current node data
                    dir = "right";
                    if(current.getRight() == null && !isPersistent) { // Add new node (non-persistent)
                        current.setRight(temp);
                        isAdded = true;
                    }else if (current.getRight() == null && isPersistent) { // Add new node (persistent)
                        persist(current, temp, dir, level, "add", true);
                        isAdded = true;
                    } else  { // Keep traversing
                        persist(current, temp, dir, level, "add", false);
                        current = current.getRight();
                    }
                    level++;
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
        boolean isPersistent = isPersistent();
        int level = 1;

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

                    if(isPersistent) {
                        persist(previous, current, "left", level, "remove", false);
                    }
                }
                level++;
            } else if(comparison > 0) { // data is greater than current node data
                Node<E> right = current.getRight();
                if(right != null) {
                    previous = current;
                    current = right;

                    // Keep track of direction of traverse
                    isRight = true;
                    isLeft = false;

                    if(isPersistent) {
                        persist(previous, current, "right", level, "remove", false);
                    }
                    level++;
                }
            } else { // Elements are equal, this is the node to be removed.
                if(isPersistent) {
                    persist(previous, current, isLeft ? "left" : "right", level, "remove", true);
                } else {
                    if(isLeft) { previous.setLeft(null);}
                    else if(isRight) {previous.setRight(null);}
                    this.size --;
                }
                isRemoved = true;
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
    abstract void persist(Node<E> persistNode, Node<E> modifyNode, String position, int level, String action, boolean modify);

    public abstract int getNumVersions();
}
