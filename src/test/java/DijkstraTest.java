package Test;

import Szukajava.Graph;
import Szukajava.Node;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DijkstraTest {
    @Test
    public void ClassicPathTest(){
        Graph graph = new Graph("src/test/java/Test.resources/ClassicPathTest.txt");
        int[] correctPath = {1,4,3,6};
        Node[] foundPath = graph.findPath(1,6);
        for (int i = 0; i < foundPath.length; i++) {
            assertEquals(correctPath[i], foundPath[i].getId());
        }
    }
    @Test
    public void OneBySevenPathTest(){
        Graph graph = new Graph("src/test/java/Test.resources/OneBySevenPathTest.txt");
        int[] correctPath = {0,1,2,3,4,5,6};
        Node[] foundPath = graph.findPath(0,6);
        for (int i = 0; i < foundPath.length; i++) {
            assertEquals(correctPath[i], foundPath[i].getId());
        }
    }
}
