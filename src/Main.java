public class Main {

    public static void main(String[] args) {
        BinarySearchTree<Integer> tree = new BinarySearchTree<>();
        tree.add(5);
        System.out.println(tree.contains(5));
        tree.remove(5);
        System.out.println(tree.contains(5));
    }
}
