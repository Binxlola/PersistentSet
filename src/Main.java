public class Main {

    public static void main(String[] args) {
        DynamicBinaryTree<Integer> tree = new DynamicBinaryTree<Integer>();
        tree.add(5);
        tree.add(3);
        tree.add(2);
        //#TODO sort out the remove for persistence
        tree.remove(2);
    }
}
