import java.io.*;
import java.util.LinkedList;
import java.util.Random;

public class Graph {
    /**Tablica węzłów grafu*/
    private Node[] nodes;
    /**Ilość grafów, na które został podzielony graf oryginalny*/
    private int nbOfGraphs;
    /**Wysokość grafu*/
    private int length;
    /**Szerokość grafu*/
    private int width;
    /**Dolny koniec przedziału, z którego są losowane wagi ścieżek*/
    private double lower;
    /**Górny koniec przedziału, z którego są losowane wagi ścieżek*/
    private double upper;

    /**
     * Generuje graf na podstawie podanych parametrów.
     *
     * @param width      szerokość grafu
     * @param length     wysokość grafu
     * @param upper      dolny koniec przedziału, z którego są losowane wagi ścieżek
     * @param lower      górny koniec przedziału, z którego są losowane wagi ścieżek
     * @param nbOfGraphs ilość grafów na które ma zostać podzielony graf spójny
     */
    public Graph(int width, int length, double upper, double lower, int nbOfGraphs) {
        if (lower < upper) {
            this.lower = lower;
            this.upper = upper;
        } else {
            this.upper = lower;
            this.lower = upper;
        }
        generateGraph(width, length, upper, lower, nbOfGraphs);
    }

    /**
     * Wczytuje graf z podanego pliku.
     *
     * @param inFile plik zawierający graf
     */
    public Graph(String inFile) throws NumberFormatException {
        this.nbOfGraphs = 1;
        readGraph(inFile);
    }

    /**
     * Zapisuje wygenerowany graf do podanego pliku.
     *
     * @param outFileName plik, do którego ma zostać zapisany graf
     */
    public void printGraphToFile(String outFileName) {
        try {
            FileWriter out = new FileWriter(outFileName);
            out.append(this.length + " " + this.width + "\n");
            for (Node currentNode : this.nodes) {
                for (int j = 0; j < currentNode.getWays(); j++) {
                    out.append(" " + currentNode.getConnAtIndex(j).getId() + " :" + currentNode.getEdgeAtIndex(j));
                }
                out.append("\n");
            }
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sprawdza czy graf jest spójny.
     *
     * @return false jeżeli nie jest spójny, true jeżeli jest
     */
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

        return checked.size() == this.nodes.length;
    }

    /**
     * Dzieli graf na kilka grafów niespójnych.
     */
    public void divideGraph() {
        Random random = new Random();
        int i, r;

        int[] ww = new int[this.nbOfGraphs];


        for (i = 0; i < this.nbOfGraphs - 1; i++) {
            while (wasDrawn(ww, (r = random.nextInt(this.width - 1)))) {
            }
            ww[i] = r;
        }


        for (i = 0; i < this.nbOfGraphs - 1; i++) {
            deleteConn(ww[i], 1);
            deleteConn(ww[i] + 1, -1);
        }
    }

    /**
     * Szuka najkrótszej możliwej ścieżki między zadanymi węzłami.
     *
     * @param start miejsce rozpączęcia szukania
     * @param end   miejsce docelowe, do którego wyznaczamy ścieżkę
     * @return Najkrótsza możliwa ścieżka
     */
    public Node[] findPath(int start, int end) {
        int tmpi = start, tmpe = end;


        while (tmpi > this.width - 1)
            tmpi -= this.width;

        while (this.nodes[tmpi].getIndexOfConnection(tmpi - 1) != -1)
            tmpi--;

        int ngwidth = 1;
        while (this.nodes[tmpi].getIndexOfConnection(tmpi + 1) != -1) {
            ngwidth++;
            tmpi++;
        }

        while (tmpe > this.width - 1)
            tmpe -= this.width;


        if (tmpe < (tmpi - ngwidth + 1) || tmpe > tmpi) {
            return new Node[0];
        }

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


        Node[] temp = new Node[ngsize];
        int k = 0;
        temp[k] = this.nodes[end];
        k++;

        while (end != start) {
            temp[k] = p[end];
            end = p[end].getId();
            k++;
        }

        Node[] path = new Node[k];


        for (int i = 0; i < k; i++) {
            path[i] = temp[k - i - 1];
        }

        return path;
    }

    /**
     * Znajduje wagę krawędzi w prawą stronę od podanego węzła.
     *
     * @param index węzęł, którego połączenia szukamy
     * @return Waga ścieżki w prawo
     */
    public double getRightValue(int index) {
        double pathToRight = -1;
        if (width != 1)
            for (int i = 0; i < this.nodes[index].getWays(); i++)
                if (index + 1 == this.nodes[index].getConnAtIndex(i).getId()) {
                    pathToRight = this.nodes[index].getEdgeAtIndex(i);
                    break;
                }
        return pathToRight;
    }

    /**
     * Znajduje wagę krawędzi w dół od podanego węzła.
     *
     * @param index węzęł, którego połączenia szukamy
     * @return Waga ścieżki w dół
     */
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

    /**
     * Generuje graf na podstawie podanych parametrów
     *
     * @param width      szerokość grafu
     * @param length     wysokość grafu
     * @param upper      górny koniec przedziału, z którego są losowane wagi ścieżek
     * @param lower      dolny koniec przedziału, z którego są losowane wagi ścieżek
     * @param nbOfGraphs liczba grafów, na które ma zostać podzielny graf spójny
     */
    private void generateGraph(int width, int length, double upper, double lower, int nbOfGraphs) {
        Node[] genNodes = new Node[width * length];
        this.width = width;
        this.length = length;

        for (int i = 0; i < length * width; i++) {
            int currentWays = determineWays(i);
            genNodes[i] = new Node(i, currentWays, new Node[currentWays], new double[currentWays]);
        }
        this.nodes = genNodes;
        this.nbOfGraphs = nbOfGraphs;

        if (this.nbOfGraphs < 1 || this.nbOfGraphs > this.width)
            this.nbOfGraphs = 1;

        for (int i = 0; i < genNodes.length; i++) {
            fillRandomConnections(i);
        }

        this.divideGraph();
    }

    /**
     * Wczytuje graf z pliku
     *
     * @param inFile plik, z którego ma zostać wczytany graf
     */
    private void readGraph(String inFile) throws NumberFormatException {
        try {
            File file = new File(inFile);
            FileReader inF = new FileReader(file);
            BufferedReader in = new BufferedReader(inF);
            String line = null;
            StringBuilder tmp = null;

            line = in.readLine();
            int i = 0;

            while (line.charAt(i) == ' ') {
                i++;
            }
            tmp = new StringBuilder();
            while (line.charAt(i) != ' ') {
                tmp.append(line.charAt(i));
                i++;
            }
            this.length = Integer.parseInt(tmp.toString());

            while (line.charAt(i) == ' ') {
                i++;
            }
            tmp = new StringBuilder();
            while (i < line.length() && line.charAt(i) != ' ' && line.charAt(i) != '\t') {
                tmp.append(line.charAt(i));
                i++;
            }
            this.width = Integer.parseInt(tmp.toString());

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
                    tmp = new StringBuilder();
                    while (line.charAt(i) == ' ' || line.charAt(i) == '\t') {
                        i++;
                    }
                    while (line.charAt(i) != ' ') {
                        tmp.append(line.charAt(i));
                        i++;
                    }

                    tmpConn[j][ways] = Integer.parseInt(tmp.toString());

                    while (line.charAt(i) != ':') {
                        i++;
                    }
                    i++;
                    tmp = new StringBuilder();
                    while (i < line.length() && line.charAt(i) != ' ') {
                        if (line.charAt(i) == ',')
                            tmp.append('.');
                        else
                            tmp.append(line.charAt(i));
                        i++;
                    }
                    tmpEdges[ways] = Double.parseDouble(tmp.toString());
                    ways++;

                    while (i < line.length() && (line.charAt(i) == ' ' || line.charAt(i) == '\t'))
                        i++;
                }
                fileNodes[j] = new Node(j, ways, new Node[ways], new double[ways]);

                for (int k = 0; k < ways; k++) {
                    fileNodes[j].setEdgeAtIndex(k, tmpEdges[k]);

                    if (tmpEdges[k] < lower)
                        lower = tmpEdges[k];
                    if (tmpEdges[k] > upper)
                        upper = tmpEdges[k];
                }
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

    /**
     * Na podstawie informacji o grafie określa ile połączeń powinien mieć podany węzęł
     *
     * @param id identyfikator podanego węzła
     * @return ilość połączeń dla podanego węzła
     */
    private int determineWays(int id) {
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

    /**
     * Losuje wagi krawędzi wychodzących z podanego węzła oraz przypisuje je w obie strony
     *
     * @param current węzęł dla którego losujemy krawędzie
     */
    private void fillRandomConnections(int current) {
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

    /**
     * Sprawdza czy numer kolumny został już wcześniej wylosowany
     *
     * @param ww tablica z wartościami int (zawierająca numery wierzchołków)
     * @param r  liczba, którą sprawdzamy czy jest zapisana w tablicy
     * @return wartość logiczną odpowiadającą na pytanie
     */
    private boolean wasDrawn(int[] ww, int r) {
        for (int i = 0; i < ww.length; i++) {
            if (ww[i] == r)
                return true;
        }
        return false;
    }

    /**
     * Usuwa połączenie węzła
     *
     * @param n numer węzła, którego połączenie ma być usunięte
     * @param i liczba, po dodaniu której do n, otrzymujemy numer węzła, do którego ma być usunięte połączenie
     */
    private void deleteConn(int n, int i) {
        for (int p = 0; p < this.length; p++) {
            Node tmpn = this.nodes[n + p * this.width];
            int id = tmpn.getIndexOfConnection(n + p * this.width + i);

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
    }

    /**
     * Tworzy kolejkę priorytetową, zaczynając od węzła startowego
     *
     * @param n id węzła, od którego ma zaczynać się kolejka priorytetowa
     * @return lista - kolejka priorytetowa z wszystkimi węzłami danego grafu, rozpoczynająca się od węzła o id=n
     */
    private LinkedList<Node> priorityQueue(int n) {
        Node current = this.nodes[n];
        LinkedList<Node> working = new LinkedList<>();
        LinkedList<Node> pq = new LinkedList<>();
        working.add(current);

        while (!working.isEmpty()) {
            for (Node node : current.getConn()) {
                if (!working.contains(node) && !pq.contains(node))
                    working.add(node);
            }
            pq.add(working.poll());
            current = working.peek();
        }

        return pq;
    }
}
