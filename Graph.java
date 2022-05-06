import javax.imageio.IIOException;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Array;
import java.util.Random;

public class Graph {
    private Node[] nodes;
    private int nbOfGraphs;
    private int length;
    private int width;

    /*Konstruktor generujący*/
    public Graph(int width, int length, double upper, double lower) {
        Node[] genNodes = new Node[width * length];
        for (int i = 0; i < length * width; i++) {
            int currentWays = determineWays(i, width, length);
            genNodes[i] = new Node(i, currentWays, new Node[currentWays], new double[currentWays]);
        }
        this.nodes = genNodes;
        this.nbOfGraphs = 1;
        this.width = width;
        this.length = length;
        if (upper < lower) {
            double temp = upper;
            upper = lower;
            lower = upper;
        }
        for (int i = 0; i < genNodes.length; i++) {
            fillRandomConnections(i, lower, upper);
        }
    }

    public Node getNodeAtIndex(int i) {
        return nodes[i];
    }

    public void printGraphToFile(String outFileName) {
        try {
            FileWriter out = new FileWriter(outFileName);
            out.append(this.width + " " + this.length + "\n");
            for (int i = 0; i < this.nodes.length; i++) {
                Node currentNode = nodes[i];
                for (int j = 0; j < currentNode.getWays(); j++) {
                    out.append("\t" + currentNode.getConnAtIndex(j).getId() + ": " + currentNode.getEdgeAtIndex(j));
                }
                out.append("\n");
            }
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int determineWays(int id, int width, int length) {
        int ways;
        if (width == 1 && length == 1)
            ways = 0;
        else if (id == 0 || id == width - 1 || id == width * length - 1 || id == width * (length - 1)) {
            if (width == 1 || length == 1)
                ways = 1;
            else
                ways = 2;
        } else if ((id > 0 && id < width - 1) || (id > (width - 1) * length && id < width * length - 1) || id % width == 0 || id % width == width - 1) {
            if (width == 1 || length == 1)
                ways = 2;
            else
                ways = 3;
        } else
            ways = 4;

        return ways;
    }

    private void fillRandomConnections(int current, double lower, double upper) {
        Random random = new Random();
        Node currentNode = this.nodes[current];
        int right = currentNode.getId() + 1;
        int left = currentNode.getId() - 1;
        int up = currentNode.getId() - this.width;
        int down = currentNode.getId() + this.width;
        int i = 0;

        if (right % width != 0 && right < nodes.length && width != 1) {
            currentNode.setConnAtIndex(i, nodes[right]);
            currentNode.setEdgeAtIndex(i, lower + (upper - lower) * random.nextDouble());
            i++;
        }
        if (down < nodes.length) {
            currentNode.setConnAtIndex(i, nodes[down]);
            currentNode.setEdgeAtIndex(i, lower + (upper - lower) * random.nextDouble());
            i++;
        }
        if (up >= 0) {
            currentNode.setConnAtIndex(i, nodes[up]);
            int index = nodes[up].getIndexOfConnection(currentNode.getId());
            if (index >= 0)
                currentNode.setEdgeAtIndex(i, nodes[up].getEdgeAtIndex(index));
            i++;
        }
        if (left % width != width - 1 && left >= 0 && width != 1) {
            currentNode.setConnAtIndex(i, nodes[left]);
            int index = nodes[left].getIndexOfConnection(currentNode.getId());
            if (index >= 0)
                currentNode.setEdgeAtIndex(i, nodes[left].getEdgeAtIndex(index));
        }
    }
}
