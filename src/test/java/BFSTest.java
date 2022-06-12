import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BFSTest {
    @Test
    public void connectedGraphTest(){
        Graph graph = new Graph("src/test/java/resources/BfsTestConnected.txt");
        assertEquals(true, graph.checkIntegrity());
    }

    @Test
    public void unconnectedGraphTest(){
        Graph graph = new Graph("src/test/java/resources/BfsTestUnconnected.txt");
        assertEquals(false, graph.checkIntegrity());
    }
}
