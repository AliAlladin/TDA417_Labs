
import java.util.Objects;


/**
 * This is a class for directed graph edges.
 */

public class DirectedEdge<Node> { 

    private final Node from;
    private final Node to;
    private final double weight;


    /**
     * Initializes a directed edge from node {@code from} to node {@code to} 
     * with the given {@code weight}.
     * @param  from    the starting node
     * @param  to      the ending node
     * @param  weight  the weight of the directed edge
     * @throws IllegalArgumentException if the edge weight is negative
     */
    public DirectedEdge(Node from, Node to, double weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
        if (weight <= 0) 
            throw new IllegalArgumentException("edge " + this + " has negative weight (or zero)");
    }


    /**
     * Initializes a directed edge from node {@code from} to node {@code to} 
     * with the default weight 1.0.
     * @param from  the starting node
     * @param to    the ending node
     */
    public DirectedEdge(Node from, Node to) {
        this(from, to, 1.0);
    }


    /**
     * @return a new edge with the direction reversed
     */
    public DirectedEdge<Node> reverse() {
        return new DirectedEdge<>(to, from, weight);
    }


    /**
     * @return the starting node of the directed edge
     */
    public Node from() {
        return from;
    }


    /**
     * @return the ending node of the directed edge
     */
    public Node to() {
        return to;
    }


    /**
     * @return the weight of the directed edge
     */
    public double weight() {
        return weight;
    }


    /**
     * @param  other  the object to compare with
     * @return true if the given argument is equal to this edge
     */
    @Override
    public boolean equals(Object other) {
        if (other == this)
            return true;
        if (!(other instanceof DirectedEdge))
            return false;
        DirectedEdge o = (DirectedEdge) other;
        return from.equals(o.from())
            && to.equals(o.to())
            && Double.compare(weight, o.weight()) == 0;
    }


    /**
     * @return the hash code of this edge
     */
    @Override
    public int hashCode() {
        return Objects.hash(from, to, Double.hashCode(weight));
    }


    /**
     * @return a string representation of the directed edge
     */
    @Override
    public String toString() {
        return String.format("%s -> %s [%s]", from, to, weight);
    }

}
