public record Edge(int u , int v , int weight) {

@Override
public int compareTo(Edge o){
    return Integer.compare(this.weight , o.weight);
    }

}