# Graph Algorithms Benchmarking

![Java](https://img.shields.io/badge/Java-17-orange.svg?style=flat-square&logo=openjdk)
![Apache Maven](https://img.shields.io/badge/Maven-3.x-C71A36.svg?style=flat-square&logo=apache-maven)

##  Project Overview

This project is a Java-based algorithmic benchmarking suite designed to empirically evaluate the performance of fundamental graph algorithms. It stress-tests Minimum Spanning Tree (MST) and Single-Source Shortest Path (SSSP) implementations against massive graph structures (up to 12.5 million edges) to observe how edge density and topology affect computational execution time in practice.

**Algorithms Implemented:**
* **Prim's Algorithm** (MST via Priority Queue)
* **Kruskal's Algorithm** (MST via Disjoint-Set / Union-Find)
* **Dijkstra's Algorithm** (General SSSP via Priority Queue)
* **DAG Shortest Path** (Linear-time SSSP via Topological Sort)

---

##  What the Program Does

When executed, the program orchestrates a complete automated benchmarking pipeline. Here is exactly how the system operates under the hood:

### 1. Topology Generation
The program dynamically synthesizes four distinct graph topologies in memory, all utilizing a fixed baseline of $V = 5,000$ vertices. 
* **Sparse Graphs:** Generates roughly 25,000 edges ($E \approx 5V$).
* **Dense Graphs:** Generates edges equal to 25% of a fully connected network ($\approx 3.1$ Million edges).
* **Complete Graphs:** Fully connects every vertex to every other vertex ($\approx 12.5$ Million edges).
* **Directed Acyclic Graphs (DAGs):** Generates a sparse, directed network where edges strictly flow from smaller to larger vertex IDs to guarantee the absence of cycles.

*(Note: To ensure valid tests, undirected graphs are initialized with a structural spanning tree to guarantee connectivity, and a 2D tracking matrix is used to prevent duplicate edges during generation).*

### 2. JVM Warm-up Phase
Before any official timing begins, the program executes two "warm-up" iterations of the algorithms. This is a critical step in Java benchmarking, as it forces the Java Virtual Machine (JVM) to load the necessary classes and triggers the Just-In-Time (JIT) compiler to optimize the bytecode, ensuring the actual benchmarks measure the algorithm's speed, not the JVM's startup time.

### 3. Execution and Measurement
For each graph topology, the program runs the competing algorithms exactly **5 times**. 
* To ensure absolute fairness, SSSP algorithms (like Dijkstra and the DAG approach) are provided the exact same randomly generated starting vertex for each comparative run.
* The execution time of each run is captured in milliseconds using high-resolution system timers.

### 4. Statistical Aggregation
Once the execution loops conclude, the program passes the collected timing data to a statistical utility. It calculates and outputs the final metrics:
* **Median Execution Time** * **Mean Execution Time**
* **Standard Deviation** (to measure the consistency and stability of the algorithm)

These metrics allow for a direct, mathematically sound comparison of how theoretical Time Complexities (like Big-O notation) translate into real-world software performance.

---

##  Core Architecture

* **`Edge.java`**: A lightweight Java record ensuring immutability and minimal memory overhead.
* **`Graph.java`**: The core data structure utilizing adjacency lists to represent the networks.
* **`UnionFind.java`**: A specialized disjoint-set data structure with path compression, utilized exclusively by Kruskal's algorithm for cycle detection.
* **`GraphGenerator.java`**: The utility class responsible for the deterministic synthesis of the specific benchmark topologies.
* **`Main.java`**: The orchestrator that handles the warm-ups, execution loops, and statistical output.
