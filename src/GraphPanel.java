import javax.swing.*;
import java.awt.*;

public class GraphPanel extends JPanel {
    private static final int MAX_DIAMETER = 50;
    private static final int MIN_DIAMETER = 10;
    private static final int DEFAULT_WIDTH = 1100;
    Graph graph;

    public GraphPanel(Graph graph) {
        super();
        this.graph = graph;
        this.setBounds(0, 74, 1100, determineHeight());
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        super.paintComponent(g2d);
        int nodeSize = determineNodeSize();
        int leftMargin = determineMargin(this.getWidth(), nodeSize, graph.getWidth());
        int upMargin = determineMargin(this.getHeight(), nodeSize, graph.getLength());
        drawNodes(g2d,nodeSize,upMargin,leftMargin);
        drawEdges(g2d,nodeSize,upMargin,leftMargin);
    }

    private void drawNodes(Graphics2D g, int nodeSize, int upMargin, int leftMargin) {
        int heightController = 0;
        for (int i = 0; i < graph.getLength(); i++) {
            int widthController = 0;
            for (int j = 0; j < graph.getWidth(); j++) {
                g.setColor(new Color(36, 36, 45));
                g.fillOval(
                        leftMargin + (widthController * nodeSize),
                        upMargin + (heightController * nodeSize),
                        nodeSize,
                        nodeSize
                );
                widthController += 2;
            }
            heightController += 2;
        }
    }

    private void drawEdges(Graphics2D g, int nodeSize, int upMargin, int leftMargin) {
        int heightController = 0;
        for (int i = 0; i < graph.getLength(); i++) {
            int widthController = 1;
            for (int j = 0; j < graph.getWidth() - 1; j++) {
                g.drawLine(
                        leftMargin + (widthController * nodeSize),
                        upMargin + (heightController * nodeSize) + nodeSize / 2,
                        leftMargin + ((widthController + 1) * nodeSize),
                        upMargin + (heightController * nodeSize) + nodeSize / 2
                );
                widthController+=2;
            }
            widthController = 0;
            heightController++;
            if (i != graph.getLength() - 1) {
                for (int j = 0; j < graph.getWidth(); j++) {
                    g.drawLine(
                            leftMargin + (widthController * nodeSize) + nodeSize / 2,
                            upMargin + heightController * nodeSize,
                            leftMargin + (widthController * nodeSize) + nodeSize / 2,
                            upMargin + (heightController + 1) * nodeSize
                    );
                    widthController += 2;
                }
            }
            heightController++;
        }
    }


    private int determineHeight() {
        int maxHeight = Toolkit.getDefaultToolkit().getScreenSize().height - 120;
        int rows = graph.getLength() * 2 + 1;
        int height;
        if ((double) (maxHeight / rows) < MIN_DIAMETER) {
            JOptionPane.showConfirmDialog(
                    null,
                    "Dimensions too big to draw",
                    "Info",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.PLAIN_MESSAGE
            );
            height = 0;
        } else if ((double) (maxHeight / rows) == MIN_DIAMETER) {
            height = maxHeight;
        } else if ((double) (maxHeight / rows) >= MAX_DIAMETER) {
            height = rows * MAX_DIAMETER;
        } else {
            double nodeSize = (double) (maxHeight / rows);
            height = (int) (rows * Math.ceil(nodeSize));
        }
        return height;
    }

    private int determineNodeSize() {
        int maxHeight = Toolkit.getDefaultToolkit().getScreenSize().height - 120;
        int rows = graph.getLength() * 2 + 1;
        int cols = graph.getWidth() * 2 + 1;
        int size;
        if ((maxHeight / rows) >= MAX_DIAMETER && (DEFAULT_WIDTH / cols) >= MAX_DIAMETER)
            size = MAX_DIAMETER;
        else if ((maxHeight / rows) == MIN_DIAMETER || (DEFAULT_WIDTH / cols) == MIN_DIAMETER)
            size = MIN_DIAMETER;
        else if ((maxHeight / rows) < MIN_DIAMETER || (DEFAULT_WIDTH / cols) < MIN_DIAMETER) {
            size = 0;
            JOptionPane.showConfirmDialog(
                    null,
                    "Dimensions too big to draw",
                    "Info",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.PLAIN_MESSAGE
            );
        } else {
            if ((maxHeight / rows) < (DEFAULT_WIDTH / cols))
                size = maxHeight / rows;
            else
                size = DEFAULT_WIDTH / cols;
        }
        return size;
    }

    private int determineMargin(int dimension, int nodeSize, int nbOfNodesPerDimension) {
        return (dimension - (nbOfNodesPerDimension * 2 - 1) * nodeSize) / 2;
    }
}
