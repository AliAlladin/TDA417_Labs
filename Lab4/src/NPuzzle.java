
import java.util.List;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;
import java.util.Arrays;

import java.util.stream.Collectors;

import java.io.IOException;


/**
 * NPuzzle is an encoding of the n-puzzle.
 *
 * The nodes are string encodings of an N x N matrix of tiles.
 * The tiles are represented by characters starting from the letter A
 * (A...H for N=3, and A...O for N=4).
 * The empty tile is represented by "_", and
 * to make it more readable for humans every row is separated by "/".
 */

public class NPuzzle implements DirectedGraph<String> {

    private int N;
    private static char separator = '/';
    private static char emptytile = '_';
    private static String tiles = "_ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";


    /**
     * Creates a new n-puzzle of size {@code N}
     * @param N  the size of the puzzle
     */
    public NPuzzle(int N) {
        if (N < 2 || N > 6) {
            throw new IllegalArgumentException("We only support sizes of 2 <= N <= 6.");
        }
        this.N = N;
    }


    /**
     * @param  s  a puzzle state
     * @return a list of the graph edges that originate from state {@code s}
     */
    public List<DirectedEdge<String>> outgoingEdges(String s) {
        List<DirectedEdge<String>> outgoing = new LinkedList<>();
        String t;
        int pos = s.indexOf(emptytile);
        int[] directions = {pos-1, pos+1, pos-(N+1), pos+(N+1)};
        for (int newpos : directions) {
            if (newpos > 0 && newpos < s.length() && s.charAt(newpos) != separator) {
                if (pos < newpos) 
                    t = s.substring(0,pos) + s.charAt(newpos) + s.substring(pos+1,newpos) +
                        emptytile + s.substring(newpos+1);
                else
                    t = s.substring(0,newpos) + emptytile + s.substring(newpos+1,pos) +
                        s.charAt(newpos) + s.substring(pos+1);
                outgoing.add(new DirectedEdge<>(s, t));
            }
        }
        return outgoing;
    }


    /**
     * @param  s  one puzzle state
     * @param  t  another puzzle state
     * @return the guessed best cost for getting from {@code s} to {@code t}
     * (the Manhattan distance between the states)
     */
    public double guessCost(String s, String t) {
        int diff = 0;
        for (int i = 0; i < s.length(); i++) {
            int j = t.indexOf(s.charAt(i)); // the position of s[i] in t
            diff += Math.abs(i-j) % (N+1);  // the number of differing horisontal tiles 
            diff += Math.abs(i-j) / (N+1);  // the number of differing vertical tiles
        }
        return diff;
    }


    /**
     * @return a legal puzzle state from the given string of tiles
     */
    private String makeState(String mytiles) {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < N; i++)
            s.append(separator + mytiles.substring(N*i, N*(i+1)));
        s.append(separator);
        return s.toString();
    }


    /**
     * @return a string representation of the puzzle graph
     */
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("NPuzzle graph of size " + N + " x " + N + ". ");
        s.append("States are strings of " + (N*N) + " chars '" + tiles.charAt(1) + "'...'" + tiles.charAt(N*N) + "', ");
        s.append("including exactly one '" + tiles.charAt(0) + "', denoting the empty tile; ");
        s.append("every " + N + " chars are interspersed with '" + separator + "'.\n\n");
        s.append("The traditional goal state is: " + makeState(tiles.substring(1,N*N) + tiles.charAt(0)) + "\n");
        s.append("Example random states and edges:\n");
        List<String> mytiles = Arrays.asList(tiles.substring(0, N*N).split(""));
        for (int i = 0; i < 10; i++) {
            Collections.shuffle(mytiles);
            String example = makeState(String.join("", mytiles));
            List<DirectedEdge<String>> edges = outgoingEdges(example);
            s.append(example + " --> " + edges.stream().map(e -> e.to()).collect(Collectors.joining(", ")) + "\n");
        }
        return s.toString();
    }


    /**
     * Unit tests the class
     * @param args  the command-line arguments
     */
    public static void main(String[] args) {
        try {
            System.out.println(new NPuzzle(Integer.valueOf(args[0])));
        } catch (Exception e) {
            // If there is an error, print it and a little command-line help
            e.printStackTrace();
            System.err.println();
            System.err.println("Usage: java NPuzzle puzzle-size");
            System.exit(1);
        }
    }

}
