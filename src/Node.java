public class Node<E> {

    private Node<E> left, right;
    private final E data;
    private int version;
    private boolean isRed; // false represents black

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

    public E getData() {return this.data;}
    public Node<E> getLeft() {return this.left;}
    public Node<E> getRight() {return this.right;}
    public void setLeft(Node<E> data) {this.left = data;}
    public void setRight(Node<E> data) {this.right = data;}
    public void setVersion(int version) {this.version = version;}
    public int getVersion() {return this.version;}

    @Override
    public String toString() {
        return this.data.toString();
    }
}
