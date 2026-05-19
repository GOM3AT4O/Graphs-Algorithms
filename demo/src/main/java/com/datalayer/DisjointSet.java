package com.datalayer;

public class DisjointSet {
    private final int[] parent; 
    private final int[] rank; // path comparssion and union rank 

    public DisjointSet(int size){
        parent = new int[size];
        rank = new int[size];

        //start with every vertex in its own set
        for(int i = 0 ; i<size ; i++){
            parent[i] = i; // each vertex is its own parent
            rank[i] = 0; // initial rank is 0
        }
    }
    //find the root of the set containing u with path compression
    public int find(int u){
        if(parent[u] ==u){
            return u ; 
        }
        //update the parent of u to its root for path compression
        parent[u] = find(parent[u]);
        return parent[u];
    }

    //unites the set containing both u and v 
    // true if first time false if they are already in the same set

    public boolean union(int u , int v){
        int rootU = find(u);
        int rootV = find(v);

        if(rootU == rootV){
            return false;
        }

        if(rank[rootU] < rank[rootV]){
            parent[rootU] = rootV;
        }
        else if(rank[rootU] > rank[rootV]){
            parent[rootV] = rootU;
        }
        else {
            parent[rootV] = rootU;
            rank[rootU]++;
        }
        return true; 
    }
}
