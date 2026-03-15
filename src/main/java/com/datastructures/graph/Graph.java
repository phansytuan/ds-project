package com.datastructures.graph;

import java.util.*;

/**
 * ============================================================
 * DATA STRUCTURE: Graph (Adjacency List)
 * ============================================================
 *
 * CONCEPT:
 *   A graph is a set of VERTICES (nodes) connected by EDGES.
 *   Unlike trees, graphs can have cycles and no "root".
 *
 *   Types of graphs:
 *   - Directed: edges have direction (A → B means A goes to B, not B to A)
 *   - Undirected: edges go both ways (A — B means A↔B)
 *   - Weighted: edges have a weight/cost
 *   - Unweighted: all edges have equal weight
 *   - Cyclic: contains cycles (loops)
 *   - Acyclic: no cycles (DAG = Directed Acyclic Graph)
 *
 *   Example undirected graph:
 *     A — B — C
 *     |       |
 *     D — E — F
 *
 *   Vertices: {A, B, C, D, E, F}
 *   Edges:    {A-B, B-C, A-D, C-F, D-E, E-F}
 *
 * REPRESENTATION OPTIONS:
 *
 *   1. Adjacency Matrix — 2D array
 *      matrix[i][j] = 1 if edge from i to j
 *      Space: O(V²) — wasteful for sparse graphs
 *      Edge check: O(1)
 *
 *   2. Adjacency List — Map<Node, List<Node>>  ← THIS IMPLEMENTATION
 *      Each node maps to its list of neighbors
 *      Space: O(V + E) — efficient for sparse graphs
 *      Edge check: O(degree)
 *
 *   Most interview problems use adjacency lists (sparse graphs are common).
 *
 * REAL-WORLD USE CASES:
 *   - Social networks (users are nodes, friendships are edges)
 *   - GPS navigation (roads are edges with weights)
 *   - Dependency resolution (npm packages, build systems)
 *   - Web page linking (PageRank algorithm)
 *   - Network routing protocols
 *
 * TIME COMPLEXITY:
 *   Add vertex      : O(1)
 *   Add edge        : O(1)
 *   BFS / DFS       : O(V + E)
 *   Shortest path (unweighted): O(V + E) via BFS
 *   Topological sort: O(V + E)
 *   Cycle detection : O(V + E)
 *
 * SPACE COMPLEXITY: O(V + E)
 *
 * INTERVIEW TIPS:
 *   - BFS = shortest path in unweighted graph
 *   - DFS = detect cycles, topological sort, connected components
 *   - Always track VISITED nodes to avoid infinite loops in cyclic graphs!
 *   - Grids/matrices are implicit graphs (each cell is a node)
 * ============================================================
 */
public class Graph {

    // ──────────────────────────────────────────────
    // FIELDS
    // ──────────────────────────────────────────────

    private final Map<Integer, List<Integer>> adjacencyList;
    private final boolean directed;

    public Graph(boolean directed) {
        this.adjacencyList = new HashMap<>();
        this.directed = directed;
    }

    // ──────────────────────────────────────────────
    // BUILDING THE GRAPH
    // ──────────────────────────────────────────────

    /** Add a vertex to the graph. */
    public void addVertex(int vertex) {
        adjacencyList.putIfAbsent(vertex, new ArrayList<>());
    }

    /**
     * Add an edge between two vertices.
     * For undirected graphs, adds edges in both directions.
     */
    public void addEdge(int from, int to) {
        addVertex(from);
        addVertex(to);

        adjacencyList.get(from).add(to);
        if (!directed) {
            adjacencyList.get(to).add(from); // Undirected: both directions
        }
    }

    /** Get all neighbors of a vertex. */
    public List<Integer> getNeighbors(int vertex) {
        return adjacencyList.getOrDefault(vertex, Collections.emptyList());
    }

    public Set<Integer> getVertices() { return adjacencyList.keySet(); }
    public int vertexCount()  { return adjacencyList.size(); }
    public boolean hasVertex(int v)        { return adjacencyList.containsKey(v); }
    public boolean hasEdge(int from, int to) {
        return adjacencyList.containsKey(from) && adjacencyList.get(from).contains(to);
    }

    // ──────────────────────────────────────────────
    // GRAPH TRAVERSALS
    // ──────────────────────────────────────────────

    /**
     * BFS (Breadth-First Search):
     * Visit nodes level by level, starting from 'start'.
     * Uses a QUEUE — explores nearest neighbors before going deeper.
     *
     * Graph:  A — B — C
     *         |       |
     *         D — E — F
     *
     * BFS from A: A → B,D → C,E → F
     * (visits by distance from A)
     *
     * Time: O(V + E)
     * Space: O(V) for the queue + visited set
     */
    public List<Integer> bfs(int start) {
        List<Integer> order = new ArrayList<>();
        if (!hasVertex(start)) return order;

        Set<Integer> visited = new HashSet<>();
        Queue<Integer> queue = new LinkedList<>();

        visited.add(start);
        queue.offer(start);

        while (!queue.isEmpty()) {
            int vertex = queue.poll(); // Take from FRONT
            order.add(vertex);

            // Enqueue all unvisited neighbors
            for (int neighbor : getNeighbors(vertex)) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.offer(neighbor); // Add to REAR
                }
            }
        }
        return order;
    }

    /**
     * DFS (Depth-First Search):
     * Explore as far as possible down each branch before backtracking.
     * Uses a STACK (or recursion — the call stack IS a stack).
     *
     * BFS from A: A → B → C → F → E → D
     * (goes deep before backtracking)
     *
     * Time: O(V + E)
     * Space: O(V) for the visited set + O(h) for recursion stack
     */
    public List<Integer> dfs(int start) {
        List<Integer> order = new ArrayList<>();
        Set<Integer> visited = new HashSet<>();
        dfsHelper(start, visited, order);
        return order;
    }

    private void dfsHelper(int vertex, Set<Integer> visited, List<Integer> order) {
        visited.add(vertex);
        order.add(vertex);

        for (int neighbor : getNeighbors(vertex)) {
            if (!visited.contains(neighbor)) {
                dfsHelper(neighbor, visited, order); // RECURSE (go deep)
            }
        }
        // Backtrack: return to caller when all neighbors are visited
    }

    // ──────────────────────────────────────────────
    // CLASSIC GRAPH ALGORITHMS
    // ──────────────────────────────────────────────

    /**
     * SHORTEST PATH (BFS-based, unweighted graphs):
     * Find the fewest edges between 'start' and 'end'.
     *
     * BFS guarantees that the FIRST time we reach a node,
     * we've taken the shortest path to it.
     *
     * Returns -1 if no path exists.
     * Time: O(V + E)
     */
    public int shortestPath(int start, int end) {
        if (!hasVertex(start) || !hasVertex(end)) return -1;
        if (start == end) return 0;

        Map<Integer, Integer> distance = new HashMap<>();
        Queue<Integer> queue = new LinkedList<>();

        distance.put(start, 0);
        queue.offer(start);

        while (!queue.isEmpty()) {
            int vertex = queue.poll();
            int dist = distance.get(vertex);

            for (int neighbor : getNeighbors(vertex)) {
                if (!distance.containsKey(neighbor)) {
                    distance.put(neighbor, dist + 1);
                    if (neighbor == end) return dist + 1; // Found it!
                    queue.offer(neighbor);
                }
            }
        }
        return -1; // No path found
    }

    /**
     * CONNECTED COMPONENTS:
     * Find all connected groups of vertices in an UNDIRECTED graph.
     * Returns a list of components, each being a list of vertices.
     *
     * Graph: A-B-C    D-E    F
     * Components: [[A,B,C], [D,E], [F]]
     *
     * Time: O(V + E)
     */
    public List<List<Integer>> connectedComponents() {
        Set<Integer> visited = new HashSet<>();
        List<List<Integer>> components = new ArrayList<>();

        for (int vertex : getVertices()) {
            if (!visited.contains(vertex)) {
                // Start a new component
                List<Integer> component = new ArrayList<>();
                bfsComponent(vertex, visited, component);
                components.add(component);
            }
        }
        return components;
    }

    private void bfsComponent(int start, Set<Integer> visited, List<Integer> component) {
        Queue<Integer> queue = new LinkedList<>();
        visited.add(start);
        queue.offer(start);

        while (!queue.isEmpty()) {
            int v = queue.poll();
            component.add(v);
            for (int neighbor : getNeighbors(v)) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.offer(neighbor);
                }
            }
        }
    }

    /**
     * CYCLE DETECTION (Directed Graph):
     * Detect if a directed graph contains a cycle.
     *
     * Uses DFS with 3 states per node:
     *   WHITE (0) = unvisited
     *   GRAY  (1) = currently in DFS stack (being processed)
     *   BLACK (2) = fully processed, no cycle through this node
     *
     * A cycle exists if we encounter a GRAY node during DFS —
     * meaning we've found a back edge to an ancestor!
     *
     * Time: O(V + E)
     */
    public boolean hasCycle() {
        Map<Integer, Integer> state = new HashMap<>();
        for (int v : getVertices()) state.put(v, 0); // All WHITE

        for (int vertex : getVertices()) {
            if (state.get(vertex) == 0) { // Unvisited
                if (dfsCycleDetect(vertex, state)) return true;
            }
        }
        return false;
    }

    private boolean dfsCycleDetect(int vertex, Map<Integer, Integer> state) {
        state.put(vertex, 1); // Mark GRAY (in progress)

        for (int neighbor : getNeighbors(vertex)) {
            if (state.get(neighbor) == 1) return true;  // Back edge → cycle!
            if (state.get(neighbor) == 0) {             // WHITE → explore
                if (dfsCycleDetect(neighbor, state)) return true;
            }
        }

        state.put(vertex, 2); // Mark BLACK (done)
        return false;
    }

    /**
     * TOPOLOGICAL SORT (Directed Acyclic Graph only):
     * Linear ordering of vertices such that for every edge u→v,
     * u comes BEFORE v in the ordering.
     *
     * Use case: Build order (compile A before B if A depends on B)
     * npm install order, course prerequisites.
     *
     * Algorithm: DFS-based (Kahn's algorithm uses BFS + in-degree)
     * After fully processing a node (all its neighbors are done),
     * PREPEND it to the result list.
     *
     * Time: O(V + E)
     */
    public List<Integer> topologicalSort() {
        Set<Integer> visited = new HashSet<>();
        LinkedList<Integer> result = new LinkedList<>();

        for (int vertex : getVertices()) {
            if (!visited.contains(vertex)) {
                topoHelper(vertex, visited, result);
            }
        }
        return result;
    }

    private void topoHelper(int vertex, Set<Integer> visited, LinkedList<Integer> result) {
        visited.add(vertex);

        for (int neighbor : getNeighbors(vertex)) {
            if (!visited.contains(neighbor)) {
                topoHelper(neighbor, visited, result);
            }
        }

        result.addFirst(vertex); // Prepend: this node comes BEFORE its dependents
    }

    /**
     * IS BIPARTITE: Can the graph be colored with 2 colors such that
     * no two adjacent nodes share the same color?
     * (Equivalent to: does the graph contain an ODD-LENGTH cycle?)
     *
     * Use case: Checking if a conflict graph can be split into two groups
     * (e.g., assignment scheduling where conflicting tasks can't share a slot).
     *
     * Time: O(V + E)
     */
    public boolean isBipartite() {
        Map<Integer, Integer> color = new HashMap<>(); // -1 = uncolored, 0 or 1 = color

        for (int start : getVertices()) {
            if (color.containsKey(start)) continue; // Already colored

            Queue<Integer> queue = new LinkedList<>();
            color.put(start, 0);
            queue.offer(start);

            while (!queue.isEmpty()) {
                int v = queue.poll();
                for (int neighbor : getNeighbors(v)) {
                    if (!color.containsKey(neighbor)) {
                        // Color neighbor with opposite color
                        color.put(neighbor, 1 - color.get(v));
                        queue.offer(neighbor);
                    } else if (color.get(neighbor).equals(color.get(v))) {
                        return false; // Same color adjacent → not bipartite
                    }
                }
            }
        }
        return true;
    }

    // ──────────────────────────────────────────────
    // UTILITY
    // ──────────────────────────────────────────────

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(
            (directed ? "Directed" : "Undirected") + " Graph:\n"
        );
        List<Integer> sorted = new ArrayList<>(adjacencyList.keySet());
        Collections.sort(sorted);
        for (int v : sorted) {
            sb.append("  ").append(v).append(" → ").append(adjacencyList.get(v)).append("\n");
        }
        return sb.toString();
    }

    // ──────────────────────────────────────────────
    // DEMO
    // ──────────────────────────────────────────────

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════╗");
        System.out.println("║           GRAPH DEMO             ║");
        System.out.println("╚══════════════════════════════════╝\n");

        // Undirected Graph:
        //   1 — 2 — 3
        //   |       |
        //   4 — 5 — 6
        System.out.println("=== Undirected Graph ===");
        Graph undirected = new Graph(false);
        undirected.addEdge(1, 2); undirected.addEdge(2, 3);
        undirected.addEdge(1, 4); undirected.addEdge(3, 6);
        undirected.addEdge(4, 5); undirected.addEdge(5, 6);
        System.out.println(undirected);

        System.out.println("BFS from 1:          " + undirected.bfs(1));
        System.out.println("DFS from 1:          " + undirected.dfs(1));
        System.out.println("Shortest path 1→6:   " + undirected.shortestPath(1, 6));
        System.out.println("Shortest path 1→3:   " + undirected.shortestPath(1, 3));
        System.out.println("Connected components: " + undirected.connectedComponents());
        System.out.println("Is Bipartite?        " + undirected.isBipartite());

        // Directed Acyclic Graph (DAG) for Topological Sort
        //   Course prerequisites: 0→1→3, 0→2→3
        System.out.println("\n=== Directed Graph (DAG) ===");
        Graph dag = new Graph(true);
        dag.addEdge(0, 1); dag.addEdge(0, 2);
        dag.addEdge(1, 3); dag.addEdge(2, 3); dag.addEdge(3, 4);
        System.out.println(dag);
        System.out.println("Topological sort: " + dag.topologicalSort());
        System.out.println("Has cycle?        " + dag.hasCycle()); // false

        // Directed Graph WITH cycle
        System.out.println("\n=== Directed Graph WITH Cycle ===");
        Graph cyclic = new Graph(true);
        cyclic.addEdge(0, 1); cyclic.addEdge(1, 2); cyclic.addEdge(2, 0); // 0→1→2→0
        System.out.println(cyclic);
        System.out.println("Has cycle?        " + cyclic.hasCycle()); // true
    }
}
