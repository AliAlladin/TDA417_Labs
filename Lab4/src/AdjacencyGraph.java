
import java.util.List;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;

import java.util.stream.Collectors;

import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Paths;


/**
 * This is a class for a generic finite graph, with string nodes. 
 *
 * The edges are stored as an adjacency list as described in 
 * the course book and the lectures. 
 * The graphs can be anything, such as a road map or a web link graph.
 * The graph can be read from a simple text file with one edge per line.
 */

public class AdjacencyGraph implements DirectedGraph<String> {

    private Map<String, List<DirectedEdge<String>>> adjacencyList;
    private int totalNrEdges;


    /**
     * Creates a new empty graph.
     */
    public AdjacencyGraph() {
        adjacencyList = new HashMap<>();
        totalNrEdges = 0;
    }


    /**
     * Creates a new graph with edges from a text file.
     * The file should contain one edge per line, each on the form 
     * "from TAB to TAB weight" or "from TAB to".
     * @param file  path to a text file
     */
    public AdjacencyGraph(String file) throws IOException {
        adjacencyList = new HashMap<>();
        totalNrEdges = 0;
        Files.lines(Paths.get(file))
            .filter(line -> !line.startsWith("#"))
            .map(line -> line.split("\t"))
            .map(edge -> (edge.length == 2
                          ? new DirectedEdge<>(edge[0].trim(), edge[1].trim())
                          : new DirectedEdge<>(edge[0].trim(), edge[1].trim(), Double.valueOf(edge[2].trim()))
                          ))
            .forEach(edge -> add(edge));
    }


    /**
     * @return the number of nodes in this graph (
     * Note: Actually, it only considers nodes that have outgoing edges!
     */
    public int nNodes() {
        return adjacencyList.size();
    }


    /**
     * @return the number of edges in this graph
     */
    public int nEdges() {
        return totalNrEdges;
    }


    /**
     * Adds the directed edge {@code e} to this edge-weighted graph.
     * Note: This does not test if the edge is already in the graph!
     * @param e  the edge
     */
    public void add(DirectedEdge<String> e) {
        List<DirectedEdge<String>> outgoing = adjacencyList.get(e.from());
        if (outgoing == null) {
            outgoing = new LinkedList<>();
            adjacencyList.put(e.from(), outgoing);
        }
        outgoing.add(e);
        totalNrEdges++;
    }


    /**
     * @param  n  a graph node
     * @return a list of the graph edges that originate from node {@code n}
     */
    public List<DirectedEdge<String>> outgoingEdges(String n) {
        List<DirectedEdge<String>> outgoing = adjacencyList.get(n);
        if (outgoing == null) 
            outgoing = new LinkedList<>();
        return outgoing;
    }


    /**
     * @param  from  the node
     * @return the degree of node {@code from}, i.e., the number of outgoing edges
     */
    public int degree(String from) {
        return this.outgoingEdges(from).size();
    }


    /**
     * @return a string representation of the graph
     */
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("Adjacency graph with " + nNodes() + " nodes, " + nEdges() + " edges\n\n");
        int ctr = 0;
        for (String v : adjacencyList.keySet()) {
            List<DirectedEdge<String>> edges = outgoingEdges(v);
            if (edges.isEmpty()) continue;
            if (ctr++ > 10) {
                // only show at most 10 edges
                s.append("(...)\n");
                break; 
            }
            s.append(v + " --> " + edges.stream()
                     .map(e -> e.to() + "[" + e.weight() + "]")
                     .collect(Collectors.joining(", ")) + "\n");
        }
        return s.toString();
    }


    /**
     * Unit tests the class
     * @param args  the command-line arguments
     */
    public static void main(String[] args) {
        try {
            System.out.println(new AdjacencyGraph(args[0]));
        } catch (Exception e) {
            // If there is an error, print it and a little command-line help
            e.printStackTrace();
            System.err.println();
            System.err.println("Usage: java AdjacencyGraph graphfile");
            System.exit(1);
        }
    }

}
