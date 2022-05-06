import java.util.Random;

public class Node {
    private final int id;
    private int ways;
    private Node[] conn;
    private double[] edges;

    public Node(int id, int ways, Node[] conn, double[] edges) {
        this.id = id;
        this.ways = ways;
        this.conn = conn;
        this.edges = edges;
    }

    public int getId() {
        return id;
    }

    public int getWays() {
        return ways;
    }

    public void fillRandomConnections(Node[] allNodes, int width, double lower, double upper) {
        Random random = new Random();
        int right = this.id + 1;
        int left = this.id - 1;
        int up = this.id - width;
        int down = this.id + width;
        int lenght = allNodes.length / width;
        int i = 0;

        if (right % width != 0 && right < allNodes.length) {
            this.conn[i] = allNodes[right];
            this.edges[i] = lower + (upper - lower) * random.nextDouble();
            i++;
        } else if (down < allNodes.length) {
            this.conn[i] = allNodes[down];
            this.edges[i] = lower + (upper - lower) * random.nextDouble();
            i++;
        } else if (up > 0) {
            this.conn[i] = allNodes[up];
            int j;
            for (j = 0; j < allNodes[up].ways; j++)
                if (allNodes[up].conn[j].id == this.id)
                    break;
            this.edges[i] = allNodes[up].edges[j];
            i++;
        } else if (left % width != width - 1 && left > 0) {
            this.conn[i] = allNodes[left];
            int j;
            for (j = 0; j < allNodes[left].ways; j++)
                if (allNodes[left].conn[j].id == this.id)
                    break;
            this.edges[i] = allNodes[left].edges[j];
        }
    }

}
