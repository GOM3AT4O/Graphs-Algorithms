
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Stack;

public class Graph implements GraphInterface {

    private final int numVertices;
    private final List<List<Edge>> adjList;


    public Graph(int numVertices){
        this.numVertices = numVertices;
        this.adjList = new ArrayList<>(numVertices);
        for(int i = 0; i<numVertices ; i++){
            adjList.add(new ArrayList<>());
        }
    }

    @Override
    public void addEdge(int u, int v, int weight) {

        //undireect so add edge for both vertices
        adjList.get(u).add(new Edge(u , v , weight));
        adjList.get(v).add(new Edge(v , u , weight));

    }
    
    @Override
    public void addDirectedEdge(int u, int v, int weight){
        
        adjList.get(u).add(new Edge(u , v , weight));

    }

    @Override
    public List<Edge> primMST() {
        List<Edge> mstEdges = new ArrayList<>();
        boolean[] visited = new boolean[numVertices];
        PriorityQueue<Edge> pq = new PriorityQueue<>();

        //start with vertex 0
        visited[0] = true;
        pq.addAll(adjList.get(0));

        while(!pq.isEmpty() && mstEdges.size() < numVertices - 1){
            Edge minEdge = pq.poll();
            int u = minEdge.u();
            int v = minEdge.v();

            // if the v is in mst then it's a circle 
            if(visited[v]){
                continue;
            }
            mstEdges.add(minEdge);
            visited[v] = true;

            for (Edge edge : adjList.get(v)){
                if(!visited[edge.v()]){
                    pq.add(edge);
                }
            }

        }
        return mstEdges;
    }

    @Override
    public List<Edge> kruskalMST() {
        List<Edge> mstEdges = new ArrayList<>();
        List<Edge> allEdges = new ArrayList<>();

        // Collect all unique edges
        for(int u =  0 ; u<numVertices ; u++){
            for(Edge edge: adjList.get(u)){
                if(u<edge.v()){
                    allEdges.add(edge);
                }
            }
        }

        //sort first
        Collections.sort(allEdges);

        //user the disjoint set
        DisjointSet df = new DisjointSet(numVertices);

        for(Edge edge: allEdges){

            if(df.union(edge.u(),edge.v())){
                mstEdges.add(edge);

                if(mstEdges.size() == numVertices - 1){
                    break;
                }
            }
        }
        return mstEdges;
    }

    //helper for the pq for dijkstra
    private record QueueNode(int vertex, int distance) implements Comparable<QueueNode> {
        @Override
        public int compareTo(QueueNode other) {
            return Integer.compare(this.distance, other.distance);
        }
    } // to know the vertex and the distance from the source

    @Override
    public int[] dijkstra(int source) {
        int[] distances = new int[numVertices];
        for(int i = 0 ; i<numVertices ; i++){
            distances[i] = Integer.MAX_VALUE;
        }

        PriorityQueue<QueueNode> pq = new PriorityQueue<>();
        distances[source] = 0;
        pq.add(new QueueNode(source, 0));

        while (!pq.isEmpty()){
            QueueNode current = pq.poll();
            int u = current.vertex();

            //ignore the long path
            if(current.distance() > distances[u]){
                continue;
            }

            for(Edge edge : adjList.get(u)){
                int v = edge.v();
                int weight = edge.weight();

                if(distances[u] + weight <distances[v]){
                    distances[v] = distances[u] +weight;
                    pq.add(new QueueNode(v , distances[v]));
                }
            }
        }
        return distances;
    }

    @Override
    public int[] dagShortestPath(int source){
        Stack<Integer> topoOrder = new Stack<>();
        int[] state = new int[numVertices]; //0 unvisited 1 visiting 2 visited

        for(int i = 0 ; i<numVertices ; i++){
            if(state[i] ==0){
                topoSortDFS(i , state , topoOrder);
            }
        }

        int[] distances = new int[numVertices];
        for(int i = 0 ; i<numVertices ; i++){
            distances[i] = Integer.MAX_VALUE;
        }

        distances[source] = 0;

        //proces in topological order
        while(!topoOrder.isEmpty()){
            int u = topoOrder.pop();

            if(distances[u] != Integer.MAX_VALUE){
                for(Edge edge : adjList.get(u)){
                    int v = edge.v();
                    if(distances[u] +edge.weight()<distances[v]){
                        distances[v] = distances[u] + edge.weight();
                    }
                }
            }
        }
        return distances;


    }

    //DFS TO FIND THE TOPOLOGICAL ORDER 
    //ALSO TO CHECK FOR THE CYCLES 
    private  void topoSortDFS(int u , int[] state,Stack<Integer> stack){
        state[u] =1 ; // mark as visiting

        for(Edge edge : adjList.get(u)){
            int v = edge.v();
            if(state[v] ==1){
                throw new IllegalStateException("Graph contains a cycle, topological sort not possible");
            }
            if(state[v]==0){
                topoSortDFS(v, state, stack);
            }
        }
        state[u] = 2; // mark as visited
        stack.push(u); // add to topo order
    }

    public int getNumVertices() {
        return numVertices;
    }
}
