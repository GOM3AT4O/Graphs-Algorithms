package com.example;
import java.util.Arrays;

import com.datalayer.Graph;
import com.utilitylayer.GraphGenerator;

public class Main {

    private static final int Runs = 5;
    private static final int WarmupRuns = 2;
    private static final int SOURCE_VERTEX = 0; //make sure to use the same source vertex for all algorithms for consistency
    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("         GRAPH ALGORITHMS BENCHMARK SUITE         ");
        System.out.println("==================================================\n");


        //1. sparse Graph 
        Graph sparseGraph = GraphGenerator.generateSparseGraph();
        benchmarkUndirectedGraph("Spare Graph (E ~ 5V)", sparseGraph);

        //2. Dense Graph
        Graph denseGraph = GraphGenerator.generateDenseGraph();
        benchmarkUndirectedGraph("Dense Graph (E ~ 25% of V*(V-1))", denseGraph);

        //3. Complete Graph
        Graph completeGraph = GraphGenerator.generateCompleteGraph();
        benchmarkUndirectedGraph("Complete Graph (E ~ V*(V-1))", completeGraph);

        //4. DAG
        Graph dagGraph = GraphGenerator.generateDAG();
        benchmarkDAG("DAG (E ~ 5V)", dagGraph);

        System.out.println("\nAll benchmarks completed successfully! good for us :)");

    }

    private static void benchmarkUndirectedGraph(String title, Graph graph){
        System.out.println("\n--------------------------------------------------");
        System.out.println("Benchmarking: " + title);
        System.out.println("--------------------------------------------------");

        //s5n eldonia 
        System.out.println("Running " + WarmupRuns + " warm-up runs to stabilize the JVM...");
        for(int i = 0 ; i<WarmupRuns ; i++){
            graph.primMST();
            graph.dijkstra(SOURCE_VERTEX);
            graph.kruskalMST();
        }

        double[] primTimes = new double[Runs];
        double[] dijkstraTimes = new double[Runs];
        double[] kruskalTimes = new double[Runs];

        for(int i = 0 ; i<Runs ; i++){

            long startTime = System.nanoTime();
            graph.primMST();
            primTimes[i] = (System.nanoTime() - startTime) / 1_000_000.0; // convert to milliseconds

            //benchmark Dijkstra's algorithm
            startTime = System.nanoTime();
            graph.dijkstra(SOURCE_VERTEX);
            dijkstraTimes[i] = (System.nanoTime() - startTime) / 1_000_000.0; // convert to milliseconds

            //benchmark Kruskal's algorithm
            startTime = System.nanoTime();
            graph.kruskalMST();
            kruskalTimes[i] = (System.nanoTime() - startTime) / 1_000_000.0; // convert to milliseconds

        }

        //print results
        printStats("Prim's MST", primTimes);
        printStats("Dijkstra's SSSP", dijkstraTimes);
        printStats("Kruskal's MST", kruskalTimes);
    }

    //Runs SSSP benchmarks specifically comparing Dijkstra to the linear-time DAG algorithm.
    private static void benchmarkDAG(String title , Graph graph){
        System.out.println("\n--------------------------------------------------");
        System.out.println("Benchmarking SSSP on DAG: " + title);
        System.out.println("--------------------------------------------------");

        System.out.println("Warming up the JVM with " + WarmupRuns + " runs...");
        for(int i = 0 ; i<WarmupRuns ; i++){
            graph.dijkstra(SOURCE_VERTEX);
            graph.dagShortestPath(SOURCE_VERTEX);
        }

        double[] dijkstraTimes = new double[Runs];
        double[] dagTimes = new double[Runs];

        for(int i = 0 ; i<Runs ; i++){

            long startTime = System.nanoTime();
            graph.dijkstra(SOURCE_VERTEX);
            dijkstraTimes[i] = (System.nanoTime() - startTime) / 1_000_000.0; // convert to milliseconds

            startTime = System.nanoTime();
            graph.dagShortestPath(SOURCE_VERTEX);
            dagTimes[i] = (System.nanoTime() - startTime) / 1_000_000.0; // convert to milliseconds
        }

        double dijkstraMean = printStats("Dijkstra's Algorithm", dijkstraTimes);
        double dagMean = printStats("DAG Shortest Path", dagTimes);

        //calculate & report the speedup of DAG algorithm over Dijkstra's
        if(dagMean > 0){
            double speedup = dijkstraMean / dagMean;
            System.out.printf("\n >>> SPEED-UP: DAG algorithm is %.2fx faster than Dijkstra's! <<<\n", speedup); 
        }
    }

    private static double printStats(String algorithmName , double[] times){
        Arrays.sort(times);
        double[] originalOrder =  times.clone(); // keep the original order for potential further analysis

        double median = times[Runs / 2]; // median as a robust measure of central tendency

        double sum = 0 ; 
        for(double t : times){
            sum += t;
        }
        double mean = sum / Runs;

        double sumSq = 0 ; 
        for(double t : times){
            sumSq += Math.pow(t - mean, 2);
        }
        double stdDev = Math.sqrt(sumSq / Runs);

        StringBuilder rawArray = new StringBuilder("[");
        for(int i = 0 ; i<originalOrder.length ; i++){
            rawArray.append(String.format("%.2f", originalOrder[i]));
            if(i < originalOrder.length - 1){
                rawArray.append(", ");
            }
        }        rawArray.append("]");

        System.out.printf("%-18s | Raw: %-38s | Median: %8.2f ms | Mean: %8.2f ms | StdDev: %6.2f ms\n", 
                          algorithmName,rawArray.toString(), median, mean, stdDev);

        return mean;
    }
}