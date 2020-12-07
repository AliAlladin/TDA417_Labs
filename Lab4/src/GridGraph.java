
import java.lang.Math;

import java.util.List;
import java.util.LinkedList;
import java.util.Collection;
import java.util.Random;

import java.util.stream.Collectors;

import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Paths;


/**
 * GridGraph is a 2d-map encoded as a bitmap, or a N x M matrix of characters. 
 *
 * Some characters are passable, and some denote obstacles. 
 * A node (also called "state") is a coordinate, i.e., a pair of integers; 
 * which is defined as the internal class GridGraph.Coord. 
 * You can move in the 4 normal directions, as well as diagonally. 
 * The edge costs are 1.0 (for N/S/E/W) and Math.sqrt(2) (for diagonal movement).
 * The graph can be read from a simple ASCII art text file.
 */

public class GridGraph implements DirectedGraph<GridGraph.Coord> {

    private char[][] grid;
    private int width;
    private int height;

    // Characters from Moving AI Lab:
    //   . - passable terrain
    //   G - passable terrain
    //   @ - out of bounds
    //   O - out of bounds
    //   T - trees (unpassable)
    //   S - swamp (passable from regular terrain)
    //   W - water (traversable, but not passable from terrain)
    // Characters from http://www.delorie.com/game-room/mazes/genmaze.cgi
    //   | - +  walls
    //   space  passable
    // Note: "-" must come last in allowedChars, because it's a regular expression

    private static String allowedChars = ".G@OTSW +|-"; 
    private static String passableChars = ".G ";


    /**
     * The internal class for coordinates, which are the graph nodes.
     */
    public static class Coord {
        public final int x, y;
        public Coord(int x, int y) {
            this.x = x; this.y = y;
        }
        @Override
        public boolean equals(Object o) {
            if (o == this) return true;
            if (!(o instanceof Coord)) return false;
            Coord other = (Coord) o;
            return this.x == other.x && this.y == other.y;
        }
        public int hashCode() {
            return (this.x << 8) ^ this.y;
        }
        public String toString() {
            return x + ":" + y;
        }
    }


    /**
     * Creates a new graph with edges from a text file.
     * The file describes the graph as ASCII art, 
     * in the format of the graph files from the Moving AI Lab.
     * @param file  path to a text file
     */
    public GridGraph(String file) throws IOException {
        grid = Files.lines(Paths.get(file))
            .filter(line -> line.matches("^[" + allowedChars + "]+$"))
            .map(line -> line.toCharArray())
            .toArray(char[][]::new);
        height = grid.length;
        width = grid[0].length;
        for (char[] row : grid)
            if (row.length != width)
                throw new IllegalArgumentException("Malformatted grid, row widths don't match.");
    }


    /**
     * @return the width of grid
     */
    public int width() {
        return width;
    }


    /**
     * @return the height of grid
     */
    public int height() {
        return height;
    }


    /**
     * @param  p  a graph coordinate
     * @return a list of the graph edges that originate from coordinate {@code p}
     */
    public List<DirectedEdge<Coord>> outgoingEdges(Coord p) {
        List<DirectedEdge<Coord>> outgoing = new LinkedList<>();
        for (int dx = -1; dx <= +1; dx++) 
            for (int dy = -1; dy <= +1; dy++) 
                if (!(dx == 0 && dy == 0)) 
                    if (passable(p.x+dx, p.y+dy)) {
                        Coord q = new Coord(p.x+dx, p.y+dy);
                        double w = Math.sqrt(dx*dx + dy*dy);
                        outgoing.add(new DirectedEdge<>(p, q, w));
                    }
        return outgoing;
    }


    /**
     * @return true if you're allowed to pass through the coordinate {@code <x,y>}
     */
    private boolean passable(int x, int y) {
        return x >= 0 && y >= 0 && x < width-1 && y < height-1 && passableChars.indexOf(grid[y][x]) >= 0;
    }


    /**
     * @param  p  one coordinate
     * @param  q  another coordinate
     * @return the guessed best cost for getting from {@code p} to {@code q}
     * (the Euclidean distance between the coordinates)
     */
    public double guessCost(Coord p, Coord q) {
        /******************************
         * TODO: Task 4               *
         * Change below this comment  *
         ******************************/
        return 0;
    }


    /**
     * @return a string representation of the grid 
     */
    public String showGrid() {
        return showGrid(new LinkedList<>());
    }


    /**
     * @param  path  a list of coordinates that constitutes a path
     * @return a string representation of the grid, with the given path shown
     */
    public String showGrid(List<Coord> path) {
        StringBuilder s = new StringBuilder();
        s.append("Bitmap graph of dimesions " + width + " x " + height + " pixels\n");
        int ctr = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) 
                s.append(path.contains(new Coord(x, y)) ? "*" : grid[y][x]);
            s.append("\n");
        }
        return s.toString();
    }


    /**
     * @return a string representation of this graph, 
     * including some random coordinates and edges
     */
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(showGrid());
        s.append("\nExample random coordinates and edges:\n");
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            int x, y;
            do {
                x = random.nextInt(width-2) + 1;
                y = random.nextInt(height-2) + 1;
            } while (!passable(x, y));
            Coord p = new Coord(x, y);
            List<DirectedEdge<Coord>> edges = outgoingEdges(p);
            s.append(p + " --> " + edges.stream()
                     .map(e -> String.format("%s[%.1f]", e.to(), e.weight()))
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
            System.out.println(new GridGraph(args[0]));
        } catch (Exception e) {
            // If there is an error, print it and a little command-line help
            e.printStackTrace();
            System.err.println();
            System.err.println("Usage: java GridGraph gridfile");
            System.exit(1);
        }
    }

}
