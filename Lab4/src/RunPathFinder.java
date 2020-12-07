

/**
 * This is the main class for finding paths in graphs.
 * 
 * Depending on the command line arguments, 
 * it creates different graphs and runs different search algorithms.
 */

public class RunPathFinder {    

    private static void testGridGraph(GridGraph graph, String algorithm, String start, String goal, boolean showGrid) {
        PathFinder<GridGraph.Coord> finder = new PathFinder<>(graph);
        String[] startC = start.split(":");
        String[] goalC = goal.split(":");
        PathFinder<GridGraph.Coord>.Result result =
            finder.search(algorithm,
                          new GridGraph.Coord(Integer.valueOf(startC[0]), Integer.valueOf(startC[1])),
                          new GridGraph.Coord(Integer.valueOf(goalC[0]), Integer.valueOf(goalC[1])));
        if (showGrid && result.success && graph.width() < 250 && graph.height() < 250)
            System.out.println(graph.showGrid(result.path));
        System.out.println(result);
    }


    public static void main(String[] args) {
        try {
            if (args.length < 5 || args.length % 2 == 0)
                throw new IllegalArgumentException();
            String algorithm = args[0], graphType = args[1], filePath = args[2];
            PathFinder<String> finder;
            for (int i = 3; i < args.length; i += 2) {
                String start = args[i], goal = args[i+1];
                System.out.println();
                switch (graphType) {

                case "AdjacencyGraph":
                    finder = new PathFinder<>(new AdjacencyGraph(filePath));
                    System.out.println(finder.search(algorithm, start, goal));
                    break;

                case "WordLadder":
                    finder = new PathFinder<>(new WordLadder(filePath));
                    System.out.println(finder.search(algorithm, start, goal));
                    break;

                case "NPuzzle":
                    finder = new PathFinder<>(new NPuzzle(Integer.valueOf(filePath)));
                    System.out.println(finder.search(algorithm, start, goal));
                    break;

                case "GridGraph":
                    testGridGraph(new GridGraph(filePath), algorithm, start, goal, true);
                    break;

                case "GridGraph-nogrid":
                    // Use this variant if you don't want to show the grid in the result
                    testGridGraph(new GridGraph(filePath), algorithm, start, goal, false);
                    break;

                default:
                    throw new IllegalArgumentException("Unknown graph type: " + graphType);
                }
                System.out.println();
            }

        } catch (Exception e) {
            // If there is an error, print it and a little command-line help
            e.printStackTrace();
            System.err.println();
            System.err.println("Usage: java RunPathFinder algorithm graphtype graph start goal");
            System.err.println("  where algorithm = random | ucs | astar");
            System.err.println("        graphtype = AdjacencyGraph | WordLadder | NPuzzle | GridGraph");
            System.exit(1);
        }
    }

}

