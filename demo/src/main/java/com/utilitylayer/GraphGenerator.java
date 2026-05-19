package com.utilitylayer;
import com.datalayer.Graph;
import java.util.Random;
public class GraphGenerator {

    public static final int V = 5000 ;
    private static final long SEED = 43;

    public static Graph generateSparseGraph(){
        int targetEdges = 5*V;
        return generateUndirectedGraph(targetEdges, "Sparse Graph");
    }

    //25% of all possible edges 
    public static Graph generateDenseGraph(){
        //total possible edges = V * (v-1) => so we want 25% of it 
        long maxEdges = (long) V * (V - 1) / 2;
        int targetEdges = (int) (maxEdges * 0.25);
        return generateUndirectedGraph(targetEdges, "Dense Graph");
    }

    // complete graph which is v * (v-1)
    public static Graph generateCompleteGraph(){
        Graph graph = new Graph(V);
        Random random = new Random(SEED);

        System.out.println("Generating the complete Graph hold on one sec....");

        //loop through pais and connect em' 
        for(int u = 0 ; u<V ; u++){
            for(int v = u + 1 ; v < V ; v++){
                int weight = random.nextInt(1000) + 1; // weights between 1 and 1000
                graph.addEdge(u, v, weight);
            }
        }
        return graph;
    }

    //DAG e ~ 5V
    public static Graph generateDAG(){
        Graph graph =  new Graph(V);
        Random random = new Random(SEED);
        boolean[][] hasEdge = new boolean[V][V];
        int targetEdges = 5 * V;
        int AddedEdges = 0; 
        System.out.println("Generating DAG hold on one sec....");

        //build the backbone of the system connecting i to i+1 to gerentee that 0 can reach everywhere
        for(int i = 0 ; i < V - 1 ; i++){
            int weight = random.nextInt(1000) + 1;
            graph.addDirectedEdge(i, i + 1, weight);
            hasEdge[i][i + 1] = true;
            AddedEdges++;
        }

        while(AddedEdges < targetEdges){
            int u = random.nextInt(V);
            int v = random.nextInt(V);

            if(u ==v){
                continue;
            }

            // To guarantee no cycles, edges must ALWAYS go from smaller to larger ID
            if(u>v){
                int temp = u ; 
                u = v ; 
                v = temp;
            }

            if(!hasEdge[u][v]){
                int weight = random.nextInt(1000) + 1;
                graph.addDirectedEdge(u, v, weight);
                hasEdge[u][v] = true;
                AddedEdges++;
            }
        }
        return graph;
    }

    //helper to build the undirected 
    private static Graph generateUndirectedGraph(int targetEdges,String graphName){
        Graph graph = new Graph(V);
        Random random = new Random(SEED);

        //2d array to track existing edges and avoid duplicates
        boolean[][] hasEdge = new boolean[V][V];
        int addedEdges = 0;
        System.out.println("Generating the "+ graphName+ "with " + targetEdges + " edges hold on one sec....");

        //build yhe backbone of the graph connecting i to i+1 to guarantee connectivity
        for(int i = 1 ; i < V ; i++){
            int ConnectTO = random.nextInt(i);
            int weight = random.nextInt(1000) + 1; // weights between 1 and 1000

            graph.addEdge(i, ConnectTO, weight);
            hasEdge[i][ConnectTO] = true;
            hasEdge[ConnectTO][i] = true;
            addedEdges++;
        }

        //add random filler edges until we hit the good target we have 
        while(addedEdges < targetEdges){
            int u = random.nextInt(V);
            int v = random.nextInt(V);

            //no duplicated edges
            if(u !=v && !hasEdge[u][v]){
                int weight = random.nextInt(1000) + 1; // weights between 1 and 1000
                graph.addEdge(u, v, weight);
                hasEdge[u][v] = true;
                hasEdge[v][u] = true;
                addedEdges++;
            }
        }
        return graph ; 
    }
    
}
