package com.datastructures.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 * A graph is a set of vertices connected by edges. This class stores, for each vertex, a list of neighbors
 * (an adjacency list). Undirected edges are stored in both directions; directed edges are stored one way.
 */
public class Graph {

    private final Map<Integer, List<Integer>> adjacencyList;
    private final boolean directed;

    public Graph(boolean directed) {
        this.adjacencyList = new HashMap<>();
        this.directed = directed;
    }

    /** Ensures {@code vertex} exists as a key with an empty neighbor list if it was missing. */
    public void addVertex(int vertex) {
        if (!adjacencyList.containsKey(vertex)) {
            adjacencyList.put(vertex, new ArrayList<>());
        }
    }

    public void addEdge(int from, int to) {
        addVertex(from);
        addVertex(to);

        adjacencyList.get(from).add(to);
        if (!directed) {
            adjacencyList.get(to).add(from);
        }
    }

    public List<Integer> getNeighbors(int vertex) {
        List<Integer> neighbors = adjacencyList.get(vertex);
        if (neighbors == null) {
            return Collections.emptyList();
        }
        return neighbors;
    }

    public Set<Integer> getVertices() {
        return adjacencyList.keySet();
    }

    public int vertexCount() {
        return adjacencyList.size();
    }

    public boolean hasVertex(int vertex) {
        return adjacencyList.containsKey(vertex);
    }

    public boolean hasEdge(int from, int to) {
        if (!adjacencyList.containsKey(from)) {
            return false;
        }
        return adjacencyList.get(from).contains(to);
    }

    /**
     * Breadth-first search: visit the start, then all its neighbors, then their neighbors, using a queue.
     */
    public List<Integer> bfs(int start) {
        List<Integer> visitOrder = new ArrayList<>();
        if (!hasVertex(start)) {
            return visitOrder;
        }

        Set<Integer> visited = new HashSet<>();
        Queue<Integer> queue = new LinkedList<>();

        visited.add(start);
        queue.offer(start);

        while (!queue.isEmpty()) {
            int vertex = queue.poll();
            visitOrder.add(vertex);

            List<Integer> neighbors = getNeighbors(vertex);
            for (int neighborIndex = 0; neighborIndex < neighbors.size(); neighborIndex++) {
                int neighbor = neighbors.get(neighborIndex);
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.offer(neighbor);
                }
            }
        }
        return visitOrder;
    }

    /**
     * Depth-first search: from each vertex, recurse into an unvisited neighbor before trying the next sibling.
     */
    public List<Integer> dfs(int start) {
        List<Integer> visitOrder = new ArrayList<>();
        Set<Integer> visited = new HashSet<>();
        dfsVisit(start, visited, visitOrder);
        return visitOrder;
    }

    private void dfsVisit(int vertex, Set<Integer> visited, List<Integer> visitOrder) {
        visited.add(vertex);
        visitOrder.add(vertex);

        List<Integer> neighbors = getNeighbors(vertex);
        for (int neighborIndex = 0; neighborIndex < neighbors.size(); neighborIndex++) {
            int neighbor = neighbors.get(neighborIndex);
            if (!visited.contains(neighbor)) {
                dfsVisit(neighbor, visited, visitOrder);
            }
        }
    }

    /** Shortest number of edges from {@code start} to {@code end} in an unweighted graph; -1 if unreachable. */
    public int shortestPath(int start, int end) {
        if (!hasVertex(start) || !hasVertex(end)) {
            return -1;
        }
        if (start == end) {
            return 0;
        }

        Map<Integer, Integer> distanceByVertex = new HashMap<>();
        Queue<Integer> queue = new LinkedList<>();

        distanceByVertex.put(start, 0);
        queue.offer(start);

        while (!queue.isEmpty()) {
            int vertex = queue.poll();
            int distanceSoFar = distanceByVertex.get(vertex);

            List<Integer> neighbors = getNeighbors(vertex);
            for (int neighborIndex = 0; neighborIndex < neighbors.size(); neighborIndex++) {
                int neighbor = neighbors.get(neighborIndex);
                if (distanceByVertex.containsKey(neighbor)) {
                    continue;
                }
                int newDistance = distanceSoFar + 1;
                distanceByVertex.put(neighbor, newDistance);
                if (neighbor == end) {
                    return newDistance;
                }
                queue.offer(neighbor);
            }
        }
        return -1;
    }

    /** Each connected component is one list of vertices (undirected graphs). */
    public List<List<Integer>> connectedComponents() {
        Set<Integer> visited = new HashSet<>();
        List<List<Integer>> components = new ArrayList<>();

        for (int vertex : getVertices()) {
            if (visited.contains(vertex)) {
                continue;
            }
            List<Integer> component = new ArrayList<>();
            bfsCollectComponent(vertex, visited, component);
            components.add(component);
        }
        return components;
    }

    private void bfsCollectComponent(int start, Set<Integer> visited, List<Integer> component) {
        Queue<Integer> queue = new LinkedList<>();
        visited.add(start);
        queue.offer(start);

        while (!queue.isEmpty()) {
            int vertex = queue.poll();
            component.add(vertex);

            List<Integer> neighbors = getNeighbors(vertex);
            for (int neighborIndex = 0; neighborIndex < neighbors.size(); neighborIndex++) {
                int neighbor = neighbors.get(neighborIndex);
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.offer(neighbor);
                }
            }
        }
    }

    /**
     * Directed cycle check: mark nodes unvisited, in-progress, or finished.
     * Stepping to an in-progress neighbor means a back-edge → cycle.
     */
    public boolean hasCycle() {
        Map<Integer, Integer> stateByVertex = new HashMap<>();
        for (int vertex : getVertices()) {
            stateByVertex.put(vertex, 0);
        }

        for (int vertex : getVertices()) {
            if (stateByVertex.get(vertex) != 0) {
                continue;
            }
            if (dfsCycle(vertex, stateByVertex)) {
                return true;
            }
        }
        return false;
    }

    private boolean dfsCycle(int vertex, Map<Integer, Integer> stateByVertex) {
        stateByVertex.put(vertex, 1);

        List<Integer> neighbors = getNeighbors(vertex);
        for (int neighborIndex = 0; neighborIndex < neighbors.size(); neighborIndex++) {
            int neighbor = neighbors.get(neighborIndex);
            int state = stateByVertex.get(neighbor);
            if (state == 1) {
                return true;
            }
            if (state == 0 && dfsCycle(neighbor, stateByVertex)) {
                return true;
            }
        }

        stateByVertex.put(vertex, 2);
        return false;
    }

    /** Depth-first postorder prepend gives a topological order for a DAG. */
    public List<Integer> topologicalSort() {
        Set<Integer> visited = new HashSet<>();
        LinkedList<Integer> order = new LinkedList<>();

        for (int vertex : getVertices()) {
            if (visited.contains(vertex)) {
                continue;
            }
            topologicalVisit(vertex, visited, order);
        }
        return order;
    }

    private void topologicalVisit(int vertex, Set<Integer> visited, LinkedList<Integer> order) {
        visited.add(vertex);

        List<Integer> neighbors = getNeighbors(vertex);
        for (int neighborIndex = 0; neighborIndex < neighbors.size(); neighborIndex++) {
            int neighbor = neighbors.get(neighborIndex);
            if (!visited.contains(neighbor)) {
                topologicalVisit(neighbor, visited, order);
            }
        }

        order.addFirst(vertex);
    }

    /** Two-coloring test: BFS assigns alternating colors; a same-color edge means not bipartite. */
    public boolean isBipartite() {
        Map<Integer, Integer> colorByVertex = new HashMap<>();

        for (int start : getVertices()) {
            if (colorByVertex.containsKey(start)) {
                continue;
            }

            Queue<Integer> queue = new LinkedList<>();
            colorByVertex.put(start, 0);
            queue.offer(start);

            while (!queue.isEmpty()) {
                int vertex = queue.poll();
                int vertexColor = colorByVertex.get(vertex);

                List<Integer> neighbors = getNeighbors(vertex);
                for (int neighborIndex = 0; neighborIndex < neighbors.size(); neighborIndex++) {
                    int neighbor = neighbors.get(neighborIndex);
                    if (!colorByVertex.containsKey(neighbor)) {
                        colorByVertex.put(neighbor, 1 - vertexColor);
                        queue.offer(neighbor);
                        continue;
                    }
                    if (colorByVertex.get(neighbor).equals(vertexColor)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(directed ? "Directed" : "Undirected");
        builder.append(" graph:\n");

        List<Integer> sortedVertices = new ArrayList<>(adjacencyList.keySet());
        Collections.sort(sortedVertices);

        for (int index = 0; index < sortedVertices.size(); index++) {
            int vertex = sortedVertices.get(index);
            builder.append("  ").append(vertex).append(" -> ").append(adjacencyList.get(vertex)).append("\n");
        }
        return builder.toString();
    }

    public static void main(String[] args) {
        System.out.println("--- Graph demo ---");

        System.out.println("Undirected graph (grid-like):");
        Graph undirected = new Graph(false);
        undirected.addEdge(1, 2);
        undirected.addEdge(2, 3);
        undirected.addEdge(1, 4);
        undirected.addEdge(3, 6);
        undirected.addEdge(4, 5);
        undirected.addEdge(5, 6);
        System.out.println(undirected);

        System.out.println("BFS from 1: " + undirected.bfs(1));
        System.out.println("DFS from 1: " + undirected.dfs(1));
        System.out.println("Shortest path 1 -> 6: " + undirected.shortestPath(1, 6));
        System.out.println("Shortest path 1 -> 3: " + undirected.shortestPath(1, 3));
        System.out.println("Connected components: " + undirected.connectedComponents());
        System.out.println("Bipartite? " + undirected.isBipartite());

        System.out.println();
        System.out.println("Directed acyclic graph:");
        Graph dag = new Graph(true);
        dag.addEdge(0, 1);
        dag.addEdge(0, 2);
        dag.addEdge(1, 3);
        dag.addEdge(2, 3);
        dag.addEdge(3, 4);
        System.out.println(dag);
        System.out.println("Topological sort: " + dag.topologicalSort());
        System.out.println("Has cycle? " + dag.hasCycle());

        System.out.println();
        System.out.println("Directed graph with cycle 0 -> 1 -> 2 -> 0:");
        Graph cyclic = new Graph(true);
        cyclic.addEdge(0, 1);
        cyclic.addEdge(1, 2);
        cyclic.addEdge(2, 0);
        System.out.println(cyclic);
        System.out.println("Has cycle? " + cyclic.hasCycle());
    }
}
