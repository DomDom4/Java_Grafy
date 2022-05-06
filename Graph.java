import java.sql.Array;

public class Graph {
    private Node[] head;
    private int nbOfGraphs;
    private int length;
    private int[] width;

    /*Konstruktor generujący*/
    public Graph(int width, int length, double upper, double lower) {
        Node[] genNodes = new Node[width * length];
        for (int i = 0; i < length * width; i++) {
            int currentWays = determineWays(i, width, length);
            genNodes[i] = new Node(i, currentWays, new Node[currentWays], new double[currentWays]);
        }
        if (upper < lower) {
            double temp = upper;
            upper = lower;
            lower = upper;
        }
        for (int i = 0; i < length * width; i++) {
            genNodes[i].fillRandomConnections(genNodes, width, lower, upper);
        }

        int[] widthArr = {width};
        this.head = new Node[1];
        this.head[0] = genNodes[0];
        this.nbOfGraphs = 1;
        this.width = widthArr;
        this.length = length;
    }

    public void printGraphToFile(){

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

}
