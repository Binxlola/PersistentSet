public class Node<E> {

    private Node<E> left, right;
    private final E data;
    private int version;
    private Boolean isRed = null; // false represents black

    public Node(E data) {
        this.data = data;
        this.left = null;
        this.right = null;
    }

    public Node(E data, Node<E> leftChild, Node<E> rightChild) {
        this.data = data;
        this.left = leftChild;
        this.right = rightChild;
    }

    public Node(E data, Node<E> leftChild, Node<E> rightChild, boolean isRed) {
        this.data = data;
        this.left = leftChild;
        this.right = rightChild;
        this.isRed = isRed;
    }

    public E getData() {return this.data;}
    public E getLeftData() {
        if(this.left != null) {return this.left.getData();}
        return null;
    }
    public E getRightData() {
        if(this.right != null) {return this.right.getData();}
        return null;
    }
    public Node<E> getLeft() {return this.left;}
    public Node<E> getRight() {return this.right;}
    public void setLeft(Node<E> data) {this.left = data;}
    public void setRight(Node<E> data) {this.right = data;}
    public void setVersion(int version) {this.version = version;}
    public int getVersion() {return this.version;}
    public boolean isRed() {return this.isRed;}
    public void setRed(boolean isRed) {this.isRed = isRed;}

    @Override
    public String toString() {
        if(this.isRed != null) {
            return String.format("%s\n%s", data.toString(), isRed ? "Red" : "Black");
        }
        return this.data.toString();
    }
}
