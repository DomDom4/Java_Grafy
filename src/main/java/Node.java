public class Node {
    /**Numer węzła*/
    private final int id;
    /**Ilość połączeń wychodzących z węzła*/
    private int ways;
    /**Węzły połączone z tym węzłem krawędziami*/
    private Node[] conn;
    /**Wagi krawędzi odpowiadające indeksem węzłam, z którymi się łączą*/
    private double[] edges;

    public Node(int id, int ways, Node[] conn, double[] edges) {
        this.id = id;
        this.ways = ways;
        this.conn = conn;
        this.edges = edges;
    }

    public int getWays() {
        return this.ways;
    }

    public int getId() {
        return this.id;
    }

    public Node[] getConn() { return this.conn; }

    public Node getConnAtIndex(int i) {
        return this.conn[i];
    }

    public double getEdgeAtIndex(int i) {
        return this.edges[i];
    }

    public int getIndexOfConnection(int id) {
        for (int i = 0; i < ways; i++) {
            if (this.conn[i].id == id)
                return i;
        }
        return -1;
    }

    public void setConnAtIndex(int i, Node dest) {
        this.conn[i] = dest;
    }

    public void setEdgeAtIndex(int i, double value) {
        this.edges[i] = value;
    }

    public void setWays(int n) { this.ways = n; }
    public void setConn(Node[] conn) {
        this.conn = conn;
    }

    public void setEdges(double[] edges) {
        this.edges = edges;
    }

}
