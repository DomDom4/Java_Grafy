import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GraphPanel extends JPanel {
    public static final int MAX_HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height - 150;
    private static final int MAX_DIAMETER = 50;
    private static final int MIN_DIAMETER = 10;
    private static final int DEFAULT_WIDTH = 1100;
    private static final int GRADIENT_HEIGHT = 30;
    Graph graph;

    public GraphPanel(Graph graph) {
        super();
        this.graph = graph;
        if (isTooBig() == true) {
            JOptionPane.showConfirmDialog(
                    null,
                    "Dimensions too big to draw",
                    "Info",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.PLAIN_MESSAGE
            );
            this.setBounds(0, 74, DEFAULT_WIDTH, 0);
        } else {
            this.setBounds(0, 74, DEFAULT_WIDTH, determineHeight() + GRADIENT_HEIGHT);
        }
        //repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        super.paintComponent(g2d);
        int nodeSize = determineNodeSize();
        int leftMargin = determineMargin(this.getWidth(), nodeSize, graph.getWidth());
        int upMargin = determineMargin(this.getHeight() - GRADIENT_HEIGHT, nodeSize, graph.getLength());

        ArrayList<NodeCircle> nodes = makeNodeCircles(leftMargin, upMargin, nodeSize);
        drawEdges(g2d, nodes);
        g.setColor(new Color(36, 36, 45));
        for (NodeCircle n : nodes) {
            g2d.fillOval(n.x, n.y, n.diameter, n.diameter);
        }
        drawGradient(g2d);
    }

    private void drawEdges(Graphics2D g, ArrayList<NodeCircle> nodes) {
        int nbOfNodes = graph.getNbOfNodes();
        int size = nodes.get(0).diameter;
        double lowerBound = this.graph.getLower();
        double upperBound = this.graph.getUpper();
        g.setStroke(new BasicStroke(4));
        for (int i = 0; i < nbOfNodes; i++) {
            double rightVal = graph.getRightValue(i);
            double downVal = graph.getDownValue(i);
            if (rightVal != -1) {
                g.setColor(getEdgeColor(lowerBound, upperBound, rightVal));
                g.drawLine(
                        nodes.get(i).x + size,
                        nodes.get(i).y + size / 2,
                        nodes.get(i).x + size * 2,
                        nodes.get(i).y + size / 2
                );
            }
            if (downVal != -1) {
                g.setColor(getEdgeColor(lowerBound, upperBound, downVal));
                g.drawLine(
                        nodes.get(i).x + size / 2,
                        nodes.get(i).y + size,
                        nodes.get(i).x + size / 2,
                        nodes.get(i).y + size * 2
                );
            }
        }
    }

    private void drawGradient(Graphics2D g) {
        JLabel low = new JLabel("0.0");
        low.setLocation(0, this.getHeight() - GRADIENT_HEIGHT);
        JLabel high = new JLabel("5.0");
        high.setLocation(DEFAULT_WIDTH, this.getHeight() - GRADIENT_HEIGHT);
        GradientPaint gp = new GradientPaint(
                0,
                0,
                new Color(255, 255, 204),
                this.getWidth(),
                0,
                new Color(212, 143, 93)
        );
        g.setPaint(gp);
        g.fillRect(0, this.getHeight() - GRADIENT_HEIGHT, DEFAULT_WIDTH, this.getHeight());
    }

    private Color getEdgeColor(double lower, double upper, double value) {
        int lightR = 255, lightG = 255, lightB = 204;
        int darkR = 212, darkG = 143, darkB = 93;
        double redStep = (lightR - darkR) / (upper - lower);
        int redVal = (int) Math.round(lightR - redStep * value);
        double greenStep = (lightG - darkG) / (upper - lower);
        int greenVal = (int) Math.round(lightG - greenStep * value);
        double blueStep = (lightB - darkB) / (upper - lower);
        int blueVal = (int) Math.round(lightB - blueStep * value);
        return new Color(redVal, greenVal, blueVal);
    }


    private int determineHeight() {
        int rows = graph.getLength() * 2 + 1;
        int height;
        if ((double) (MAX_HEIGHT / rows) < MIN_DIAMETER) {
            height = 0;
        } else if ((double) (MAX_HEIGHT / rows) == MIN_DIAMETER) {
            height = MAX_HEIGHT;
        } else if ((double) (MAX_HEIGHT / rows) >= MAX_DIAMETER) {
            height = rows * MAX_DIAMETER;
        } else {
            double nodeSize = (double) (MAX_HEIGHT / rows);
            height = (int) (rows * Math.ceil(nodeSize));
        }
        return height;
    }

    private int determineNodeSize() {
        int rows = graph.getLength() * 2 + 1;
        int cols = graph.getWidth() * 2 + 1;
        int size;
        if ((MAX_HEIGHT / rows) >= MAX_DIAMETER && (DEFAULT_WIDTH / cols) >= MAX_DIAMETER)
            size = MAX_DIAMETER;
        else if ((MAX_HEIGHT / rows) == MIN_DIAMETER || (DEFAULT_WIDTH / cols) == MIN_DIAMETER)
            size = MIN_DIAMETER;
        else if ((MAX_HEIGHT / rows) < MIN_DIAMETER || (DEFAULT_WIDTH / cols) < MIN_DIAMETER) {
            size = 0;
        } else {
            if ((MAX_HEIGHT / rows) < (DEFAULT_WIDTH / cols))
                size = MAX_HEIGHT / rows;
            else
                size = DEFAULT_WIDTH / cols;
        }
        return size;
    }

    private int determineMargin(int dimension, int nodeSize, int nbOfNodesPerDimension) {
        return (dimension - (nbOfNodesPerDimension * 2 - 1) * nodeSize) / 2;
    }

    private boolean isTooBig() {
        boolean outcome = false;
        if ((graph.getWidth() * 2 + 1) * MIN_DIAMETER > DEFAULT_WIDTH || (graph.getLength() * 2 + 1) * MIN_DIAMETER > MAX_HEIGHT)
            outcome = true;
        return outcome;
    }

    private ArrayList<NodeCircle> makeNodeCircles(int leftMargin, int upMargin, int nodeSize) {
        ArrayList<NodeCircle> nodes = new ArrayList<>();
        int heightController = 0;
        for (int i = 0; i < graph.getLength(); i++) {
            int widthController = 0;
            for (int j = 0; j < graph.getWidth(); j++) {
                nodes.add(new NodeCircle(
                        leftMargin + (widthController * nodeSize),
                        upMargin + (heightController * nodeSize),
                        nodeSize
                ));
                widthController += 2;
            }
            heightController += 2;
        }
        return nodes;
    }

    private class NodeCircle {
        int x;
        int y;
        int diameter;

        NodeCircle(int x, int y, int diameter) {
            this.x = x;
            this.y = y;
            this.diameter = diameter;
        }
    }
}
