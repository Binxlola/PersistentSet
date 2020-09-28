public class Node<E> {

    private Node<E> left, right;
    private final E data;

    public Node(E data) {
        this.data = data;
        this.left = null;
        this.right = null;
    }

    public E getData() {return this.data;}
    public Node<E> getLeft() {return this.left;}
    public Node<E> getRight() {return this.right;}
    public void setLeft(Node<E> data) {this.left = data;}
    public void setRight(Node<E> data) {this.right = data;}

    @Override
    public String toString() {
        return "";
    }
}
