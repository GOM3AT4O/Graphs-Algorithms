package com.datalayer;

public record Edge(int u , int v , int weight) implements Comparable<Edge> {

    @Override
    public int compareTo(Edge o){
        return Integer.compare(this.weight , o.weight);
    }

}
