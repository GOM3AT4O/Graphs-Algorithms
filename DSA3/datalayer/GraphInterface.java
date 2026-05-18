
import java.util.List;
public interface GraphInterface {

    //add undirect edge 
    void addEdge(int u , int v , int weight);

    //add a direct edge
    void addDirectedEdge(int u , int v , int weight);

    //prim's algrthim
    List<Edge> primMST();

    //kruskal's algrthim
    List<Edge> kruskalMST();

    //Returns an array of shortest path distances from the source to all other vertices.
    int[] dijkstra(int source);

    //return an array using DAG
    int[] dagShortestPath(int source);
}
