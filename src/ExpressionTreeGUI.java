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

/**

 * Almost Complete GUI - just need to finish the code when pressing the buttons and updating
 * the number of nodes in the tree.. WIll only build once ExpNode subclasses are made
 * @author seth hall
 *
 **/

public class ExpressionTreeGUI extends JPanel implements ActionListener {

    private final JButton addButton, removeButton;

    private DrawPanel drawPanel;
    private SetupPanel setupPanel;
    private AbstractBinaryTree<String> tree;
    private int numberNodes = 0;
    private JTextField dataField;
    public static int PANEL_H = 500;
    public static int PANEL_W = 700;
    private JLabel nodeCounterLabel;
    private final int BOX_SIZE = 40;

    public void start(AbstractBinaryTree<String> tree) {
        this.tree = tree;
        remove(setupPanel);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setPreferredSize(new Dimension(PANEL_W, 30));
        buttonPanel.add(dataField);
        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        add(drawPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        nodeCounterLabel = new JLabel("Number of Nodes: " + 0);
        add(nodeCounterLabel, BorderLayout.NORTH);
        revalidate();
    }

    public ExpressionTreeGUI() {
        super(new BorderLayout());
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        }
        super.setPreferredSize(new Dimension(PANEL_W, PANEL_H + 30));

        setupPanel = new SetupPanel(this);
        drawPanel = new DrawPanel();

        addButton = new JButton("Add Node");
        removeButton = new JButton("Remove Node");

        addButton.addActionListener((ActionListener) this);
        removeButton.addActionListener((ActionListener) this);
        dataField = new JTextField(40);

        super.add(setupPanel, BorderLayout.CENTER);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        Object source = event.getSource();

        if (source == addButton) {   //finish this button event to handle the evaluation and output to infix of the tree
            tree.add(dataField.getText().trim());
            dataField.setText(null);

        } else if(source == removeButton) {
            tree.remove(dataField.getText().trim());
            dataField.setText(null);
        }
        drawPanel.repaint();
    }

    private class SetupPanel extends JPanel implements ActionListener {

        private JRadioButton binary, binaryPersistent, redBlackPersistent;
        private JButton confirm;
        private ExpressionTreeGUI parent;

        public SetupPanel(ExpressionTreeGUI parent) {
            super();
            this.parent = parent;
            super.setBackground(Color.WHITE);
            super.setPreferredSize(new Dimension(PANEL_W, PANEL_H));
            setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

            binary = new JRadioButton("Binary Tree");
            binary.setBackground(null);
            binary.setSize(150, 50);
            binary.setAlignmentX(Component.CENTER_ALIGNMENT);
            binary.setAlignmentY(Component.CENTER_ALIGNMENT);
            binary.addActionListener((ActionListener) this);
            binary.setSelected(true);

            binaryPersistent = new JRadioButton("persistent Binary Tree");
            binaryPersistent.setBackground(null);
            binaryPersistent.setSize(150, 50);
            binaryPersistent.setAlignmentX(Component.CENTER_ALIGNMENT);
            binaryPersistent.setAlignmentY(Component.CENTER_ALIGNMENT);
            binaryPersistent.addActionListener((ActionListener) this);

            redBlackPersistent = new JRadioButton("Persistent Red Black Tree");
            redBlackPersistent.setBackground(null);
            redBlackPersistent.setSize(150, 50);
            redBlackPersistent.setAlignmentX(Component.CENTER_ALIGNMENT);
            redBlackPersistent.setAlignmentY(Component.CENTER_ALIGNMENT);
            redBlackPersistent.addActionListener((ActionListener) this);

            confirm = new JButton("Start");
            confirm.setSize(150, 50);
            confirm.setAlignmentX(Component.CENTER_ALIGNMENT);
            confirm.setAlignmentY(Component.BOTTOM_ALIGNMENT);
            confirm.addActionListener((ActionListener) this);

            add(Box.createRigidArea(new Dimension(0, 42)));
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
                AbstractBinaryTree tree = null;

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
                drawTree(g, getWidth());
            }
        }

        public void drawTree(Graphics g, int width) {
            drawNode(g, tree.getRoot(), BOX_SIZE, 0, 0, new HashMap<Node<? extends Object>, Point>());
        }

        private int drawNode(Graphics g, Node current,
                             int x, int level, int nodeCount, Map<Node<? extends Object>, Point> map) {


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

            g.setColor(Color.red);
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