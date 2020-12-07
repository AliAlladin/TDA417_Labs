
import java.util.List;


/**
 * A simplistic interface for directed graphs.
 *
 * Note that this interface differs from the graph interface in the course API,
 * in that it lacks several of the methods in the course API.
 */

public interface DirectedGraph<Node> {

    /**
     * @param  n  a graph node
     * @return a list of the graph edges that originate from node {@code n}
     */
    public List<DirectedEdge<Node>> outgoingEdges(Node n);

    /**
     * @param  n  one node
     * @param  m  another node
     * @return the guessed best cost for getting from {@code n} to {@code m}
     * (the default guessed cost is 0, because this is always admissible)
     */
    default double guessCost(Node n, Node m) {
        return 0;
    }

}
