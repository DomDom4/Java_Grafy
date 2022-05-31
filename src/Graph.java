import java.io.*;
import java.lang.String;
import java.util.LinkedList;
import java.util.Random;

public class Graph {
    private Node[] nodes;
    private int nbOfGraphs;
    private int length;
    private int width;
    private double lower;
    private double upper;

    /*Konstruktor generujący*/
    public Graph(int width, int length, double upper, double lower) {
        generateGraph(width, length, upper, lower);
        this.nbOfGraphs = 1;
        if (lower < upper) {
            this.lower = lower;
            this.upper = upper;
        } else {
            this.upper = lower;
            this.lower = upper;
        }
    }

    /*Konstruktor czytający*/
    public Graph(String inFile) throws NumberFormatException {
        this.nbOfGraphs = 1;
        readGraph(inFile);
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

    public boolean checkIntegrity() {
        Node current = nodes[0];
        LinkedList<Node> working = new LinkedList<>();
        LinkedList<Node> checked = new LinkedList<>();
        working.add(current);

        while (!working.isEmpty()) {
            for (Node node : current.getConn()) {
                if (!working.contains(node) && !checked.contains(node))
                    working.add(node);
            }
            checked.add(working.poll());
            current = working.peek();
        }

        if (checked.size() == this.nodes.length)
            return true;
        return false;
    }

    public void divideGraph() {
        if (this.nbOfGraphs > this.width)
            System.out.println("Error");

        Random random = new Random();

        int i, j, r;

        int[] ww = new int[this.nbOfGraphs]; //tablica węzłów początkowych (width), których ścieżki mają być usunięte

        //Wpisywanie do tablic indeksów początkowych węzłów do dzielenia
        for (i = 0; i < this.nbOfGraphs - 1; i++) {
            while (wasDrawn(ww, i, (r = random.nextInt(this.width)))) {
            }//losowanie dopóki nie będzie węzła, którego jeszcze nie było
            ww[i] = r;
        }

        //Usuwanie połączeń węzłów
        for (i = 0; i < this.nbOfGraphs - 1; i++) {
            for (j = 0; j < this.length; j++) {
                deleteConn(ww[i], 1);
                deleteConn(ww[i] + 1, -1);
            }
        }
    }

    public Node[] findPath(int start, int end) {
        int tmpi = start;

        // potrzebne gdy graf jest podzielony
        while (tmpi > this.width - 1)
            tmpi -= this.width;

        while (this.nodes[tmpi].getIndexOfConnection(tmpi - 1) != -1)
            tmpi--;

        int ngwidth = 1;
        while (this.nodes[tmpi].getIndexOfConnection(tmpi + 1) != -1) {
            ngwidth++;
            tmpi++;
        }
        // --

        int gsize = this.width * this.length;

        int ngsize = ngwidth * this.length;
        LinkedList<Node> Q = priorityQueue(start);

        double[] d = new double[gsize];
        Node[] p = new Node[gsize];

        for (int i = 0; i < gsize; i++) {
            d[i] = Double.POSITIVE_INFINITY;
            p[i] = null;
        }

        d[start] = 0;
        Node c;

        while (!Q.isEmpty()) {
            c = Q.poll();
            for (int i = 0; i < c.getWays(); i++) {
                if (d[c.getConnAtIndex(i).getId()] > d[c.getId()] + c.getEdgeAtIndex(i)) {
                    d[c.getConnAtIndex(i).getId()] = d[c.getId()] + c.getEdgeAtIndex(i);
                    p[c.getConnAtIndex(i).getId()] = c;
                }
            }
        }

        double val_s = d[end]; // długość ścieżki
        Node[] temp = new Node[ngsize];
        int k = 0;
        temp[k] = this.nodes[end];
        k++;

        //zapisanie ścieżki do tablicy pomocniczej temp
        while (end != start) {
            temp[k] = p[end];
            end = p[end].getId();
            k++;
        }

        Node[] path = new Node[k];

        //przepisanie ścieżki w poprawnej kolejności do tablicy path
        for (int i = 0; i < k; i++) {
            path[i] = temp[k - i - 1];
            //System.out.print(path[i].getId() + "->");
        }
        //System.out.println(val_s);
        return path;
    }

    public double getRightValue(int index) {
        double value = -1;
        if (width != 1)
            for (int i = 0; i < this.nodes[index].getWays(); i++)
                if (index + 1 == this.nodes[index].getConnAtIndex(i).getId()) {
                    value = this.nodes[index].getEdgeAtIndex(i);
                    break;
                }
        return value;
    }

    public double getDownValue(int index) {
        double value = -1;
        if (length != 1)
            for (int i = 0; i < this.nodes[index].getWays(); i++)
                if (index + this.width == this.nodes[index].getConnAtIndex(i).getId()) {
                    value = this.nodes[index].getEdgeAtIndex(i);
                    break;
                }
        return value;
    }

    public Node[] getNodes() {
        return this.nodes;
    }

    public double getLower() {
        return this.lower;
    }

    public double getUpper() {
        return this.upper;
    }

    public int getLength() {
        return this.length;
    }

    public int getWidth() {
        return this.width;
    }

    public int getNbOfNodes() {
        return this.nodes.length;
    }

    private void generateGraph(int width, int length, double upper, double lower) {
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
            lower = temp;
        }
        for (int i = 0; i < genNodes.length; i++) {
            fillRandomConnections(i, lower, upper);
        }
    }

    private void readGraph(String inFile) throws NumberFormatException {
        try {
            File file = new File(inFile);
            FileReader inF = new FileReader(file);
            BufferedReader in = new BufferedReader(inF);
            String line = null;
            String tmp = null;

            line = in.readLine();
            int i = 0;

            while (line.charAt(i) == ' ') {
                i++;
            }
            tmp = "";
            while (line.charAt(i) != ' ') {
                tmp += line.charAt(i);
                i++;
            }
            this.width = Integer.parseInt(tmp);

            while (line.charAt(i) == ' ') {
                i++;
            }
            tmp = "";
            while (i < line.length()) {
                tmp += line.charAt(i);
                i++;
            }
            this.length = Integer.parseInt(tmp);

            Node[] fileNodes = new Node[this.width * this.length];
            int[][] tmpConn = new int[this.width * this.length][4];
            double[] tmpEdges = new double[4];
            double lower = Double.POSITIVE_INFINITY;
            double upper = 0;

            for (int j = 0; j < this.width * this.length; j++) {
                int ways = 0;
                i = 0;
                line = in.readLine();

                while (i < line.length()) {
                    tmp = "";
                    while (line.charAt(i) == ' ' || line.charAt(i) == '\t') {
                        i++;
                    }
                    while (line.charAt(i) != ' ') {
                        tmp += line.charAt(i);
                        i++;
                    }

                    tmpConn[j][ways] = Integer.parseInt(tmp);

                    while (line.charAt(i) != ':') {
                        i++;
                    }
                    i++;
                    tmp = "";
                    while (i < line.length() && line.charAt(i) != ' ') {
                        if (line.charAt(i) == ',')
                            tmp += '.';
                        else
                            tmp += line.charAt(i);
                        i++;
                    }
                    tmpEdges[ways] = Double.parseDouble(tmp);
                    ways++;
                }
                fileNodes[j] = new Node(j, ways, new Node[ways], new double[ways]);

                for (int k = 0; k < ways; k++) {
                    fileNodes[j].setEdgeAtIndex(k, tmpEdges[k]);
                    if (tmpEdges[k] < lower)
                        lower = tmpEdges[k];
                    if (tmpEdges[k] > upper)
                        upper = tmpEdges[k];
                }

                /*System.out.print(fileNodes[j].getId() + ": ");
                for (int k = 0; k < fileNodes[j].getWays(); k++) {
                    System.out.print(fileNodes[j].getEdgeAtIndex(k) + "(");
                    System.out.print(fileNodes[j].getConnAtIndex(k) + ") ");
                }*/
            }
            for (int j = 0; j < this.width * this.length; j++)
                for (int k = 0; k < fileNodes[j].getWays(); k++)
                    fileNodes[j].setConnAtIndex(k, fileNodes[tmpConn[j][k]]);

            this.nodes = fileNodes;
            this.upper = upper;
            this.lower = lower;
            in.close();
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
        } else if ((id > 0 && id < width - 1) || (id > width * (length - 1) && id < width * length - 1) || id % width == 0 || id % width == width - 1) {
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

    private boolean wasDrawn(int[] ww, int n, int r) {
        for (int i = 0; i < n; i++) {
            if (ww[i] == r)
                return true;
        }
        return false;
    }

    private void deleteConn(int n, int i) {
        Node tmpn = this.nodes[n];
        int id = tmpn.getIndexOfConnection(n + i);

        if (id == -1)
            System.out.println("Error");

        Node[] tmpC = new Node[tmpn.getWays() - 1];
        double[] tmpE = new double[tmpn.getWays() - 1];

        int k = 0;

        for (int j = 0; j < tmpn.getWays(); j++) {
            if (j != id) {
                tmpC[k] = tmpn.getConnAtIndex(j);
                tmpE[k] = tmpn.getEdgeAtIndex(j);
                k++;
            }
        }
        tmpn.setConn(tmpC);
        tmpn.setEdges(tmpE);
        tmpn.setWays(tmpn.getWays() - 1);
    }

    private LinkedList<Node> priorityQueue(int n) {
        Node current = this.nodes[n];
        LinkedList<Node> working = new LinkedList<>();
        LinkedList<Node> checked = new LinkedList<>();
        working.add(current);

        while (!working.isEmpty()) {
            for (Node node : current.getConn()) {
                if (!working.contains(node) && !checked.contains(node))
                    working.add(node);
            }
            checked.add(working.poll());
            current = working.peek();
        }

        return checked;
    }
}
