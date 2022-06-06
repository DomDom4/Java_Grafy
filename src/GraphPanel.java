import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class GraphPanel extends JPanel implements MouseListener {
    public static final int MAX_HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height - 150;
    private static final int MAX_DIAMETER = 50;
    private static final int MIN_DIAMETER = 10;
    private static final int DEFAULT_WIDTH = 1100;
    private static final int GRADIENT_HEIGHT = 30;
    Graph graph;
    ArrayList<NodeCircle> nodes = new ArrayList<>();
    NodeCircle[] clickedNodes = new NodeCircle[2];
    Node[] path = new Node[0];

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
            addMouseListener(this);
        }
        //repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        NodeCircle tmpn = null;

        for (NodeCircle n : nodes) {
            if(x > n.x && x < n.x + n.diameter && y > n.y && y < n.y + n.diameter)
                tmpn = n;
        }

        if(clickedNodes[0] == tmpn)
            clickedNodes[0] = null;
        else if(clickedNodes[1] == tmpn)
            clickedNodes[1] = null;
        else if(clickedNodes[0] == null)
            clickedNodes[0] = tmpn;
        else if(clickedNodes[1] == null)
            clickedNodes[1] = tmpn;

        repaint();
    }
    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        super.paintComponent(g2d);
        int nodeSize = determineNodeSize();
        int leftMargin = determineMargin(this.getWidth(), nodeSize, graph.getWidth());
        int upMargin = determineMargin(this.getHeight() - GRADIENT_HEIGHT, nodeSize, graph.getLength());
        boolean tmp;

        makeNodeCircles(leftMargin, upMargin, nodeSize);
        drawEdges(g2d);

        for (NodeCircle n : nodes) {
            if(clickedNodes[0] == n || clickedNodes[1] == n)
                g2d.setColor(Color.RED);
            else if(clickedNodes[0] != null && clickedNodes[1] != null) {
                tmp = false;
                for (Node node : path) {
                    if (node.getId() == n.id) {
                        g2d.setColor(Color.ORANGE);
                        tmp = true;
                        break;
                    }
                }
                if(!tmp)
                    g.setColor(new Color(36, 36, 45));
            } else
                g.setColor(new Color(36, 36, 45));

            g2d.fillOval(n.x, n.y, n.diameter, n.diameter);
        }
        drawGradient(g2d);
    }

    private void drawEdges(Graphics2D g) {
        int nbOfNodes = graph.getNbOfNodes();
        int size = nodes.get(0).diameter;
        double lowerBound = this.graph.getLower();
        double upperBound = this.graph.getUpper();
        boolean isPath = false, isPathR, isPathD;
        if(clickedNodes[0] != null && clickedNodes[1] != null) {
            path = graph.findPath(clickedNodes[0].id, clickedNodes[1].id);
            isPath = true;
        }

        g.setStroke(new BasicStroke(4));
        for (int i = 0; i < nbOfNodes; i++) {
            int j = 0;
            isPathR = false;
            isPathD = false;
            while(isPath && j<path.length) {
                if(path[j].getId() == i) {
                    if(j != 0) {
                        if(path[j].getId() + 1 == path[j-1].getId()) {
                            isPathR = true;
                        }
                        if(path[j].getId() + graph.getWidth() == path[j-1].getId()) {
                            isPathD = true;
                        }
                    }
                    if(j != path.length-1) {
                        if (path[j].getId() + 1 == path[j + 1].getId()) {
                            isPathR = true;
                        }
                        if(path[j].getId() + graph.getWidth() == path[j+1].getId()) {
                            isPathD = true;
                        }
                    }
                }
                j++;
            }

            double rightVal = graph.getRightValue(i);
            double downVal = graph.getDownValue(i);
            if (rightVal != -1) {
                if(isPathR)
                        g.setColor(Color.BLUE);
                else
                    g.setColor(getEdgeColor(lowerBound, upperBound, rightVal));
                g.drawLine(
                        nodes.get(i).x + size,
                        nodes.get(i).y + size / 2,
                        nodes.get(i).x + size * 2,
                        nodes.get(i).y + size / 2
                );
            }
            if (downVal != -1) {
                if(isPathD)
                    g.setColor(Color.BLUE);
                else
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

    private void makeNodeCircles(int leftMargin, int upMargin, int nodeSize) {
        if(nodes.isEmpty()) {
            int heightController = 0;
            for (int i = 0; i < graph.getLength(); i++) {
                int widthController = 0;
                for (int j = 0; j < graph.getWidth(); j++) {
                    nodes.add(new NodeCircle(
                            leftMargin + (widthController * nodeSize),
                            upMargin + (heightController * nodeSize),
                            nodeSize,
                            i * graph.getWidth() + j
                    ));
                    widthController += 2;
                }
                heightController += 2;
            }
        }
    }

    private class NodeCircle {
        int x;
        int y;
        int diameter;
        int id;

        NodeCircle(int x, int y, int diameter, int id) {
            this.x = x;
            this.y = y;
            this.diameter = diameter;
            this.id = id;
        }
    }
}
