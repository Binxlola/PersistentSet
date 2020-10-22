//package exptree;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;

public class ExpressionTreeGUI extends JPanel implements ActionListener {

    private final JButton addButton, removeButton;

    private final DrawPanel drawPanel;
    private final SetupPanel setupPanel;
    private AbstractBinaryTree<Integer> tree;
    public int numberNodes = 0;
    private final JTextField dataField;
    public static int PANEL_H = 500;
    public static int PANEL_W = 700;
    private JLabel nodeCounterLabel;
    private final int BOX_SIZE = 40;
    private int selectedVersion = 0;

    public void start(AbstractBinaryTree<Integer> tree) {
        this.tree = tree;

        remove(setupPanel);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setPreferredSize(new Dimension(PANEL_W, 30));
        buttonPanel.add(dataField);
        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        add(drawPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Node count display
        nodeCounterLabel = new JLabel();
        this.displayNodeCount(numberNodes);

        add(nodeCounterLabel, BorderLayout.NORTH);
        revalidate();
    }

    public ExpressionTreeGUI() {
        super(new BorderLayout());
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("An error has occurred");
        }
        super.setPreferredSize(new Dimension(PANEL_W, PANEL_H + 30));

        setupPanel = new SetupPanel(this);
        drawPanel = new DrawPanel();

        addButton = new JButton("Add Node");
        removeButton = new JButton("Remove Node");

        addButton.addActionListener(this);
        removeButton.addActionListener( this);
        dataField = new JTextField(40);

        super.add(setupPanel, BorderLayout.CENTER);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        Object source = event.getSource();

        if (source == addButton) {   //finish this button event to handle the evaluation and output to infix of the tree
            tree.add(Integer.parseInt(dataField.getText().trim()));
            if(tree.isPersistent()) {selectedVersion++;}
            dataField.setText(null);

        } else if(source == removeButton) {
            tree.remove(Integer.parseInt(dataField.getText().trim()));
            if(tree.isPersistent()) {selectedVersion++;}
            dataField.setText(null);
        }
        drawPanel.repaint();
    }

    private void displayNodeCount(int count) {
        nodeCounterLabel.setText("Number of Nodes: " + count);
    }

    private static class SetupPanel extends JPanel implements ActionListener {

        private final JRadioButton binary, binaryPersistent, redBlackPersistent;
        private final JButton confirm;
        private final ExpressionTreeGUI parent;

        public SetupPanel(ExpressionTreeGUI parent) {
            super();
            this.parent = parent;
            super.setBackground(Color.WHITE);
            super.setPreferredSize(new Dimension(PANEL_W, PANEL_H));
            setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

            JLabel treeType = new JLabel("Select the tree type:");
            treeType.setBackground(null);
            treeType.setSize(150, 50);
            treeType.setAlignmentX(Component.CENTER_ALIGNMENT);
            treeType.setAlignmentY(Component.CENTER_ALIGNMENT);

            binary = new JRadioButton("Binary Tree");
            binary.setBackground(null);
            binary.setSize(150, 50);
            binary.setAlignmentX(Component.CENTER_ALIGNMENT);
            binary.setAlignmentY(Component.CENTER_ALIGNMENT);
            binary.addActionListener(this);
            binary.setSelected(true);

            binaryPersistent = new JRadioButton("persistent Binary Tree");
            binaryPersistent.setBackground(null);
            binaryPersistent.setSize(150, 50);
            binaryPersistent.setAlignmentX(Component.CENTER_ALIGNMENT);
            binaryPersistent.setAlignmentY(Component.CENTER_ALIGNMENT);
            binaryPersistent.addActionListener(this);

            redBlackPersistent = new JRadioButton("Persistent Red Black Tree");
            redBlackPersistent.setBackground(null);
            redBlackPersistent.setSize(150, 50);
            redBlackPersistent.setAlignmentX(Component.CENTER_ALIGNMENT);
            redBlackPersistent.setAlignmentY(Component.CENTER_ALIGNMENT);
            redBlackPersistent.addActionListener(this);

            JLabel valueType = new JLabel("Select the value type:");
            valueType.setBackground(null);
            valueType.setSize(150, 50);
            valueType.setAlignmentX(Component.CENTER_ALIGNMENT);
            valueType.setAlignmentY(Component.CENTER_ALIGNMENT);

            confirm = new JButton("Start");
            confirm.setSize(150, 50);
            confirm.setAlignmentX(Component.CENTER_ALIGNMENT);
            confirm.setAlignmentY(Component.BOTTOM_ALIGNMENT);
            confirm.addActionListener(this);

            add(Box.createRigidArea(new Dimension(0, 42)));
            add(treeType);
            add(Box.createRigidArea(new Dimension(0, 5)));
            add(binary);
            add(Box.createRigidArea(new Dimension(0, 5)));
            add(binaryPersistent);
            add(Box.createRigidArea(new Dimension(0, 5)));
            add(redBlackPersistent);

            add(Box.createRigidArea(new Dimension(0, 42)));
            add(confirm);
        }

        @Override
        public void actionPerformed(ActionEvent event) {
            Object source = event.getSource();
            Component[] components = this.getComponents();

            if(source.equals(confirm)) {
                JRadioButton selected = null;

                // Find the select option
                for(Component component : components) {
                    if(component instanceof JRadioButton && ((JRadioButton) component).isSelected()) {
                        selected = (JRadioButton) component;
                        break;
                    }
                }

                // Start the main application with the correct tree
                assert selected != null;
                if(selected.equals(binary)) {parent.start(new BinarySearchTree<>());}
                else if(selected.equals(binaryPersistent)) {parent.start(new DynamicBinaryTree<>());}
                else if(selected.equals(redBlackPersistent)) {parent.start(new DynamicRedBlackTree<>());}

            } else {
                for(Component component : components) {
                    if(!component.equals(source) && component instanceof JRadioButton) {
                        ((JRadioButton) component).setSelected(false);
                    }
                }
            }
        }
    }

    private class DrawPanel extends JPanel {

        public DrawPanel() {
            super();
            super.setBackground(Color.WHITE);
            super.setPreferredSize(new Dimension(PANEL_W, PANEL_H));
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (tree.getRoot() != null) {
                drawTree(g );
            }
        }

        public void drawTree(Graphics g) {
            if(tree.getRoot() != null) {
               nodeCounterLabel.setText("Number of Nodes: " + drawNode(g, tree.getRoot(), BOX_SIZE, 0, 0, new HashMap<>()));
            }
        }

        private int drawNode(Graphics g, Node<Integer> current,
                             int x, int level, int nodeCount, Map<Node<?>, Point> map) {


            //recursive call
            if (current.getLeft() != null) {
                nodeCount = drawNode(g, current.getLeft(), x, level + 1, nodeCount, map);
            }

            int currentX = x + nodeCount * BOX_SIZE;
            int currentY = level * 2 * BOX_SIZE + BOX_SIZE;
            nodeCount++;
            map.put(current, new Point(currentX, currentY));

            if (current.getRight() != null) {
                nodeCount = drawNode(g, current.getRight(), x, level + 1, nodeCount, map);
            }

            g.setColor(current.getVersion() == selectedVersion && tree.isPersistent() ? Color.green : Color.red);
            if (current.getLeft() != null) {
                Point leftPoint = map.get(current.getLeft());
                g.drawLine(currentX, currentY, leftPoint.x, leftPoint.y - BOX_SIZE / 2);
            }
            if (current.getRight() != null) {
                Point rightPoint = map.get(current.getRight());
                g.drawLine(currentX, currentY, rightPoint.x, rightPoint.y - BOX_SIZE / 2);

            }

            Point currentPoint = map.get(current);
            g.fillRect(currentPoint.x - BOX_SIZE / 2, currentPoint.y - BOX_SIZE / 2, BOX_SIZE, BOX_SIZE);
            g.setColor(Color.BLACK);
            g.drawRect(currentPoint.x - BOX_SIZE / 2, currentPoint.y - BOX_SIZE / 2, BOX_SIZE, BOX_SIZE);
            Font f = new Font("courier new", Font.BOLD, 12);
            g.setFont(f);
            g.drawString(current.toString(), currentPoint.x-3, currentPoint.y);
            return nodeCount;

        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Expression Tree GUI builder");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new ExpressionTreeGUI());
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dimension = toolkit.getScreenSize();
        int screenHeight = dimension.height;
        int screenWidth = dimension.width;
        frame.pack();             //resize frame appropriately for its content
        //positions frame in center of screen
        frame.setLocation(new Point((screenWidth / 2) - (frame.getWidth() / 2),
                (screenHeight / 2) - (frame.getHeight() / 2)));
        frame.setVisible(true);


    }
}
