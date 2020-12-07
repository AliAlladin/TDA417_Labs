# Lab 4: Path finder

In this lab your task is to write a program that calculates the shortest path between two nodes in a graph. You will try this on several different kinds of graphs.


## About the labs

- Labs must be done in groups. If you absolutely cannot work together with your previous lab partners, contact Christian, Peter or Nick.
- Each lab has two deadlines. By the first deadline you must submit at least a partial attempt at solving the lab. The lab will be marked and you will get feedback. By the second deadline, you must submit a complete and correct solution. If you are unsure if you have solved the lab correctly, please ask at a lab session!
- The lab is part of the examination of the course. Therefore, you must not copy code from or show code to other groups. You are welcome to discuss ideas with one another, but anything you submit must be **the work of you and your lab partners**.
- Please read [the general instructions on how to run the labs](https://chalmers.instructure.com/courses/10681/pages/running-the-labs-command-line-eclipse-or-intellij).


## Getting started

Start by downloading and unzipping **[Lab4.zip](https://chalmers.instructure.com/courses/10681/files/folder/Labs)**. It contains a number of Java files (explained below), as well as a directory of **graphs**, and a file **answers.txt** where you will write down answers to questions in the lab.


## Background

Your task is to write a program that calculates the shortest path between two nodes in a graph. This is similar to what map applications do (e.g. Waze, OpenStreetMap, Google, Apple, Garmin, etc), but your solution will not be able to handle as large roadmaps as them. But this is also useful in several other circumstances, and you will see some in this laboration.

There is a test program which you can compile and run rightaway. It takes five arguments:

- the algorithm ("random", "ucs", or "astar")
- the type of graph to read ("AdjacencyGraph", "NPuzzle", "GridGraph", or "WordLadder")
- the graph itself (usually a file, but for the NPuzzle it's a number denoting the size of the puzzle)
- the start node
- the goal node

Currently, the only algorithm that works is "random", which does a random walk in the graph, hoping to find the goal. When the program has found a path, it is printed on standard output, together with the total cost and some statistics:

```
$ javac RunPathFinder.java
$ java RunPathFinder random AdjacencyGraph graphs/AdjacencyGraph/citygraph-VGregion.txt Göteborg Mölndal
Loop iterations: 44
Elapsed time: 0.001 seconds
Total cost from Göteborg -> Mölndal: 562.0
Total path length: 44
Path: Göteborg -> Torslanda -> Björlanda -> Andalen -> Partille -> ... -> Torslanda -> Landvetter -> Surte -> Eriksbo -> Mölndal
```

Note that since it does a random walk, the returned path will be different every time. Also, if you try to run it on a more difficult problem, such as Göteborg to Götene, it will run forever. (Or, it would if it didn't exit after 1 million iterations).


## Descriptions of the Java classes

The following Java classes are in the collection you downloaded:

- **RunPathFinder**
- **PathFinder**
- **DirectedEdge**
- **DirectedGraph** is an interface with the implementing classes:
  - **AdjacencyGraph**
  - **NPuzzle**
  - **GridGraph**
  - **WordLadder**

### RunPathFinder

This is the main test class, which was described previously in the background. It consists of one single method:

    RunPathFinder:
        void main(String[] args)

### PathFinder

This is the class that does the heavy path finding. It consists of some searching methods, together with inner classes for priority queue entries and search results, and an auxiliary method for extracing the solution path:

    PathFinder<Node>:
        Result search(String algorithm, Node start, Node end)
        Result searchRandom(Node start, Node end)
        Result searchUCS(Node start, Node end)    // this is Task 1a+c
        Result searchAstar(Node start, Node end)  // this is Task 4

        List<Node> extractPath(PQEntry entry)     // this is Task 1b

        class PQEntry             // you will extend this in Task 4
        class Result

### DirectedEdge

A weighted directed edge, consisting of two nodes and a weight. If the weight isn't provided it's assumed to be 1.0.

    DirectedEdge<Node>:
        Node from()
        Node to()
        double weight()

### DirectedGraph

This is an interface with only two methods:

    interface DirectedGraph<Node>:
        List<DirectedEdge<Node>> outgoingEdges(Node from)
        double guessCost(Node from, Node goal)

The methods are described later. The **PathFinder** assumes that the graph implements this interface, which all graphs below do.

Note that this interface differs from the graph interface in the course API, because it lacks several of the API methods. But it is enough for the purposes in this lab.

### AdjacencyGraph

The **AdjacencyGraph** reads a generic finite graph, with one edge per line, and stores it as an adjacency list as described in the course book and the lectures. The graphs can be anything, and in the graph repository there are distance graphs for several countries, including EU and Västra Götaland (VGregion), called "citygraph-XX.txt". But there is also a link graph between more than 4500 Wikipedia pages, "wikipedia-graph.txt":

```
$ java RunPathFinder random AdjacencyGraph graphs/AdjacencyGraph/wikipedia-graph.txt Sweden Norway
Loop iterations: 175
Elapsed time: 0.001 seconds
Total cost from Sweden -> Norway: 174.0
Total path length: 175
Path: Sweden -> Potato -> Stefan_Edberg -> German_language -> Parliamentary_system -> ... -> Parliamentary_system -> Eastern_Orthodox_Church -> Chile -> Lithuania -> Norway
```

### NPuzzle

**NPuzzle** is an encoding of the [n-puzzle](https://en.wikipedia.org/wiki/15_puzzle), where the states are string encodings of an *N* x *N* matrix. The tiles are represented by chars starting from the letter A (`A`…`H` for *N*=3, and `A`…`O` for *N*=4). The empty tile is represented by `_`, and to make it more readable for humans every row is separated by `/`. Like this:

```
$ java RunPathFinder ucs NPuzzle 2 "/_C/BA/" "/AB/C_/"
Loop iterations: 22
Elapsed time: 0.058 seconds
Total cost from /_C/BA/ -> /AB/C_/: 6.0
Total path length: 7
Path: /_C/BA/ -> /C_/BA/ -> /CA/B_/ -> /CA/_B/ -> /_A/CB/ -> /A_/CB/ -> /AB/C_/

$ java RunPathFinder ucs NPuzzle 3 "/ABC/DEF/HG_/" "/ABC/DEF/GH_/"
Loop iterations: 483841
Elapsed time: 0.561 seconds
No path found from /ABC/DEF/HG_/
```

Note that the current implementation of the UCS algorithm is just a stub that never finds anything, your task 1 is to fix that. (And it's no use trying the "random" algorithm on the **NPuzzle**: it will almost certainly never return anything).

### GridGraph

**GridGraph** is a 2d-map encoded as a bitmap, or a *N* x *M* matrix of characters. Some characters are passable, and some denote obstacles. A node (also called "state") is a coordinate, i.e., a pair of integers; which is defined as an internal class `GridGraph.Coord`. You can move in the 4 normal directions, as well as diagonally. The edge costs are 1.0 (for N/S/E/W) and Math.sqrt(2) (for diagonal movement). From the command line you write a coordinate as `x:y`, like this:

```
$ java RunPathFinder ucs GridGraph graphs/GridGraph/maze-10x5.txt "1:1" "39:9"
Bitmap graph of dimesions 41 x 11 pixels
+---+---+---+---+---+---+---+---+---+---+
 ****   |   |           *********   |   |
+---+*  +   +   +---+  *+---+---+*  +   +
|   |*  |           | * |   |   *       |
+   +*  +---+---+---+*  +   +  *+---+---+
|    *          |   *   |   |   *****   |
+---+*  +---+---+  *+---+   +---+   +*  +
|   *   |   *******         |       |*  |
+  *+---+  *+---+---+---+---+   +---+ * +
|   ******* |                   |      * 
+---+---+---+---+---+---+---+---+---+---+
Loop iterations: 973
Elapsed time: 0.008 seconds
Total cost from 1:1 -> 39:9: 58.87005768508879
Total path length: 52
Path: 1:1 -> 2:1 -> 3:1 -> 4:1 -> 5:2 -> ... -> 36:5 -> 37:6 -> 37:7 -> 38:8 -> 39:9
```

### WordLadder

This class is unfinished, and in Task 2 you will complete it.

> "Word ladder is a word game invented by Lewis Carroll. A word ladder puzzle begins with two words, and to solve the puzzle one must find a chain of other words to link the two, in which two adjacent words (that is, words in successive steps) differ by one letter."
([Wikipedia](https://en.wikipedia.org/wiki/Word_ladder))

We denote this problem as a graph, where the nodes are the words and the edges denote one step in this word ladder.

The class does not store the full graph in memory, just the dictionary of words. The edges are then computed on demand. The class already contains code that reads the dictionary, but you must complete the rest of the class.

### Differences from the code in the course book

There are two main differences between the class of graphs in this lab and the one in the course book:

1. The nodes don't have to be integers, but can be anything. The **AdjacencyGraph** uses string nodes, **NPuzzle** uses matrices encoded as strings, **GridGraph** uses pairs of integers, and **WordLadder** uses strings. (Note that this is in line with the lecture slides)
2. The graphs don't have to be known in advance. **AdjacencyGraph** knows all its edges, but the other kinds of graphs calculate the edges on demand.

Both these differences have important impact on Dijkstra's search algorithm as it is described in the course book. More about this later.


## About the graphs in the collection

#### AdjacencyGraph:

- All graphs **citygraph-XX.txt** are extracted from freely available [mileage charts](https://www.mileage-charts.com/). The smallest graph has 130 cities and 838 edges (citygraph-VGregion.txt), and the largest one 996 cities and 28054 edges (citygraph-EU.txt). All edge costs are in kilometers.
  - Suggested searches: `Göteborg` to `Götene` (**citygraph-VGregion.txt**); `Lund` to `Kiruna` (SE); `Porto, Portugal` to `Vorkuta, Russia` (**citygraph-EU.txt**)

- **wikipedia-graph.txt** is converted from [the Wikispeedia dataset](http://snap.stanford.edu/data/wikispeedia.html) in SNAP (Stanford Large Network Dataset Collection). It contains 4587 Wikipedia pages and 119882 page links. All edges have cost 1.
  - Suggested search: `Superconductivity` to `Anna_Karenina`

#### NPuzzle:

- **NPuzzle** doesn't need a file for initialisating the graph, just a number giving the size of the puzzle. Larger sizes than 3 are usually too difficult, even for the algorithms in this lab.
  - Suggested search for size 2: `/_C/BA/` to goal `/AB/C_/`
  - Suggested searches for size 3: any of `/_AB/CDE/FGH/`, `/CBA/DEF/_HG/`, `/FDG/HE_/CBA/` or `/HFG/BED/C_A/`, to the goal `/ABC/DEF/GH_/`
  - Try also the following size 3 puzzle, which doesn't have a solution: `/ABC/DEF/HG_/` to `/ABC/DEF/GH_/`

#### GridGraph:

- **AR0011SR.map** and **AR0012SR.map** are taken from the [2D Pathfinding Benchmarks](https://www.movingai.com/benchmarks/grids.html) in Nathan Sturtevant's Moving AI Lab. The maps are from the collection "Baldurs Gate II Original maps", and are grids of sizes 216 x 224 and 148 x 139, respectively. There are also associated PNG files, so that you can see how they look like.
  - Suggested searches: `23:161` to `130:211` (**AR0011SR.map**); `11:73` to `85:127` (**AR0012SR.map**)

- **maze-10x5.txt**, **maze-20x10.txt**, and **maze-100x50.txt** are generated by a [random maze generator](http://www.delorie.com/game-room/mazes/genmaze.cgi). They are grids of sizes 41 x 11, 81 x 21, and 201 x 101, respectively.
  - Suggested searches: `1:1` to `39:9` (**maze-10x5.txt**); `1:1` to `79:19` (maze-20x10.txt); `1:1` to `199:99` (**maze-100x50.txt**)

#### WordLadder:

- **words-romaner.txt** and **words-saldo.txt** are two Swedish word lists compiled from [Språkbanken Text](https://spraakbanken.gu.se/resurser). They both contain more words than are read by **WordLadder**, but the final graphs contain 75,740 words (**words-romaner.txt**) and 888,275 words (**words-saldo.txt**), respectively.
  - Suggested searches (after you have completed task 2 below): `eller` to `glada` (**words-romaner.txt**); `njuta` to `övrig` (**words-saldo.txt**)
  - Another interesting combination is to try any combination of the following words: `ämnet`, `åmade`, `örter`, `öring` (**words-romaner.txt**)


## Task 1: The Uniform Cost Search algorithm

There is a skeleton method `searchUCS` in **PathFinder**. Your goal in Task 1 is to implement the Uniform Cost Search algorithm. This is a variant of Dijkstra's algorithm, which can handle infinite and very large graphs. It is also arguably easier to understand than the usual formulation of Dijkstra's.


### Task 1a: The simple UCS algorithm

The main data structure used in the UCS algorithm is a priority queue. It contains graph nodes paired with the current best cost to reach them. We store this information as the inner class `PQEntry` (which is already implemented):

    class PQEntry:
        Node node
        double costToHere
        PQEntry backPointer

The `backPointer` is necesary for recreating the final path from the start node to the goal. More about this in Task 1b below. The very first entry will not have any previous entry, so then we set `baskPointer` to `null`.

Here is pseudocode of the simplest version of the UCS algorithm:

    Result searchUCS(Node start, Node goal):
        pqueue = new PriorityQueue(comparing costToHere)
        pqueue.add(new PQEntry for start)
        while pqueue is not empty:
            entry = pqueue.remove()
            if entry.node == goal: 
                SUCCESS:) extract the path and return it
            for edge in graph.outgoingEdges(entry.node):
                costToNext = entry.costToHere + edge.weight
                pqueue.add(new PQEntry for edge.to)
        FAILURE:( no solution found

It is important that we return as soon as we reach the goal, because otherwise we will continue adding new entries to the queue infinitely. 

Implement this algorithm in the `searchUCS` method. There is a variable `iterations` already defined, which you should increase every time you remove an entry from `pqueue`. When you have done this you should be able to run some simple queries, such as finding the shortest route to Kungälv, Lerum, or Alingsås:

```
$ java RunPathFinder ucs AdjacencyGraph graphs/AdjacencyGraph/citygraph-VGregion.txt Göteborg Alingsås
Loop iterations: 47205
Elapsed time: 0.061 seconds
Total cost from Göteborg -> Alingsås: 51.0
Total path length: 1
Path: Alingsås
```

But there are two problems with this implementation: (1) the path only contains the goal node, and (2) it becomes extremely slow on more difficult problems (e.g., try to find the way to Vara). We will address these problems in Tasks 1b and 1c.


### Task 1b: Extracing the solution

Now you should write code to extract the solution, which is the list of nodes forming the shortest path. So, you should modify the skeleton method `extractPath`:

    List<Node> extractPath(PQEntry entry)

To do this you only have to iterate through the backpointers and collect the nodes in a list. Just make sure that you return the nodes in the right order and not reversed!

After this is completed you should be able to run this:

```
$ java RunPathFinder ucs AdjacencyGraph graphs/AdjacencyGraph/citygraph-VGregion.txt Göteborg Alingsås
Loop iterations: 47205
Elapsed time: 0.06 seconds
Total cost from Göteborg -> Alingsås: 51.0
Total path length: 5
Path: Göteborg -> Partille -> Lerum -> Stenkullen -> Alingsås
```
As you can see, exactly the same result as before, but now the path is correct too.


### Task 1c: Remembering visited nodes

The reason why the algorithm is slow is that it will revisit the same node several times. There are 100's of ways to get from Göteborg to Stenkullen, and the algorithm visits most of them before it finds its way to Alingsås. But all the subsequent visits to Stenkullen are unnecessary, since the first one is already via the shortest path.

Therefore, a very simple solution is to record the visited nodes in a set. Wrap the for loop inside an if clause, testing that the node hasn't already been visited. And don't forget to add the node to the visited nodes, otherwise there won't be much of an optimisation. 

When this is done you should see a drastic improvement:

```
$ java RunPathFinder ucs AdjacencyGraph graphs/AdjacencyGraph/citygraph-VGregion.txt Göteborg Alingsås
Loop iterations: 177
Elapsed time: 0.005 seconds
Total cost from Göteborg -> Alingsås: 51.0
Total path length: 5
Path: Göteborg -> Partille -> Lerum -> Stenkullen -> Alingsås
```
The number of iterations went down by a factor of 3! And you should be able to solve all kinds of problems in adjacency graphs, n-puzzles, and grid graphs:

```
$ java RunPathFinder ucs AdjacencyGraph graphs/AdjacencyGraph/citygraph-EU.txt "Volos, Greece" "Oulu, Finland"
Loop iterations: 23515
Elapsed time: 0.04 seconds
Total cost from Volos, Greece -> Oulu, Finland: 3488.0
Total path length: 13
Path: Volos, Greece -> Timişoara, Romania -> Arad, Romania -> Oradea, Romania -> Debrecen, Hungary -> ... -> Lublin, Poland -> Białystok, Poland -> Tallinn, Estonia -> Helsinki, Finland -> Oulu, Finland

$ java RunPathFinder ucs NPuzzle 3 "/_AB/CDE/FGH/" "/ABC/DEF/GH_/"
Loop iterations: 185088
Elapsed time: 0.316 seconds
Total cost from /_AB/CDE/FGH/ -> /ABC/DEF/GH_/: 22.0
Total path length: 23
Path: /_AB/CDE/FGH/ -> /A_B/CDE/FGH/ -> /ADB/C_E/FGH/ -> /ADB/_CE/FGH/ -> /ADB/FCE/_GH/ -> ... -> /ABC/GDE/_HF/ -> /ABC/_DE/GHF/ -> /ABC/D_E/GHF/ -> /ABC/DE_/GHF/ -> /ABC/DEF/GH_/

$ java RunPathFinder ucs GridGraph graphs/GridGraph/maze-100x50.txt "1:1" "199:99"
(...)
Loop iterations: 26478
Elapsed time: 0.027 seconds
Total cost from 1:1 -> 199:99: 1216.4793641885828
Total path length: 1017
Path: 1:1 -> 1:2 -> 1:3 -> 1:4 -> 2:5 -> ... -> 196:97 -> 197:97 -> 198:97 -> 199:98 -> 199:99
```
Go on! Try the suggestions for the different graphs in the section "About the graphs in the collection" above!

***Important***: Make sure you get the total costs and path lengths that are shown in these examples. If you got a higher cost, then UCS didn't find the most optimal path. If you got a lower cost, then there's an error in how you calculate the path costs (or you take some illegal shortcuts). Furthermore, your implementation should visit the same number of nodes as shown (plus/minus a small constant).


## Task 2: Word ladders

The class **WordLadder** is not fully implemented, and this task is to make it work correctly. What is implemented is the reading of the dictionary, adding of words, and some auxiliary methods. What is missing is the implementation of `outgoingEdges`:

    public List<DirectedEdge<String>> outgoingEdges(String word)

One action in a word ladder is to change exactly one letter in a word, and make sure that the resulting string is also a word (according to the dictionary). Every step should cost 1.0.

At your help are the following two instance variables that are updated by the `addWord` method:

    private Set<String> dictionary
    private Set<Character> charset

After your implementation is completed, you should be able to solve the following word ladders:

```
$ java RunPathFinder ucs WordLadder graphs/WordLadder/words-romaner.txt mamma pappa
Loop iterations: 888
Elapsed time: 0.082 seconds
Total cost from mamma -> pappa: 6.0
Total path length: 7
Path: mamma -> mumma -> summa -> sumpa -> pumpa -> puppa -> pappa

$ java RunPathFinder ucs WordLadder graphs/WordLadder/words-romaner.txt katter hundar
Loop iterations: 9036
Elapsed time: 0.283 seconds
Total cost from katter -> hundar: 14.0
Total path length: 15
Path: katter -> kanter -> tanter -> tanten -> tanden -> ... -> randas -> randad -> rundad -> rundar -> hundar

$ java RunPathFinder ucs WordLadder graphs/WordLadder/words-romaner.txt örter öring
Loop iterations: 20127
Elapsed time: 0.372 seconds
Total cost from örter -> öring: 30.0
Total path length: 31
Path: örter -> arter -> artar -> antar -> andar -> ... -> slang -> klang -> kling -> kring -> öring
```


## Task 3: The A* algorithm

The UCS algorithm finds the optimal path, but there is an optimisation which (in the best case) finds the same path, only much faster! This algorithm is called A*, and you can read about it [on Wikipedia](https://en.wikipedia.org/wiki/A*_search_algorithm). Your task is to implement the metod `searchAstar` in **PathFinder**:

    public Result searchAstar(Node start, Node goal)

The basic code is much like UCS, so you can start by copying your UCS code to this method. But the A* algorithm keeps track of two scores: one which is the cost so far, just as UCS, and another which is the estimated total cost from the start, via this node, to the goal.

To work, A* needs a *heuristics*, which is an educated guess of the distance between two nodes. This requires some additional insight into the problem, so the heuristics are different for different types of graphs and problems. Our graph API (the interface **DirectedGraph**) provides this heuristics in the form of the method `guessCost`, which takes two nodes as argument and returns a cost estimate:

    public double guessCost(Node n, Node m)

If we don't have a way of guessing the cost, we should return 0. In that case the A* algorithm behaves exactly like UCS. That's why all graphs have defaulted `guessCost` to return 0. Except **NPuzzle**, which comes with a ready-baked heuristics (see next task for information about that heuristics) based on the [Manhattan distance](https://en.wikipedia.org/wiki/Taxicab_geometry) between the nodes. This is a standard, and very efficient, heuristics for this problem.

When you have implemented A*, you can therefore try it out for **NPuzzle** problems:

```
$ java RunPathFinder ucs NPuzzle 3 "/CBA/DEF/_HG/" "/ABC/DEF/GH_/"
Loop iterations: 268844
Elapsed time: 0.41 seconds
Total cost from /CBA/DEF/_HG/ -> /ABC/DEF/GH_/: 24.0
Total path length: 25
Path: /CBA/DEF/_HG/ -> /CBA/DEF/H_G/ -> /CBA/D_F/HEG/ -> /C_A/DBF/HEG/ -> /_CA/DBF/HEG/ -> ... -> /_AB/DEC/GHF/ -> /A_B/DEC/GHF/ -> /AB_/DEC/GHF/ -> /ABC/DE_/GHF/ -> /ABC/DEF/GH_/

$ java RunPathFinder astar NPuzzle 3 "/CBA/DEF/_HG/" "/ABC/DEF/GH_/"
Loop iterations: 7461
Elapsed time: 0.089 seconds
Total cost from /CBA/DEF/_HG/ -> /ABC/DEF/GH_/: 24.0
Total path length: 25
Path: /CBA/DEF/_HG/ -> /CBA/_EF/DHG/ -> /_BA/CEF/DHG/ -> /B_A/CEF/DHG/ -> /BA_/CEF/DHG/ -> ... -> /AC_/DBF/GEH/ -> /A_C/DBF/GEH/ -> /ABC/D_F/GEH/ -> /ABC/DEF/G_H/ -> /ABC/DEF/GH_/
```

Note that A* visits much fewer nodes, but otherwise it behaves just as UCS. If your implementation doesn't, then there's probably a bug somewhere.

For the other graph types, `guessCost` returns 0, so both UCS and A* should visit exactly the same number of nodes. Try that! If they differ you have a bug.


## Task 4: Guessing the cost

The graph API method `guessCost` should return a best guess of the cost between two nodes. This is already implemented for **NPuzzle**, but is missing for the other graph types. The implementation for **NPuzzle** estimates the cost as the sum of [Manhattan distances](https://en.wikipedia.org/wiki/Taxicab_geometry) for every tile to its goal position. This is the implementation:

    double guessCost(String s, String t):
        diff = 0
        for i in 0...(s.length-1):
            j = t.indexOf(s[i])            // the position of s[i] in t
            diff += Math.abs(i-j) % (N+1)  // the number of differing horisontal tiles 
            diff += Math.abs(i-j) / (N+1)  // the number of differing vertical tiles
        return diff

Your task is to implement the following `guessCost` heuristics for **GridGraph** and **WordLadder**:

- **GridGraph**: The [straight-line distance](https://en.wikipedia.org/wiki/Euclidean_distance) between the two nodes. After implementing you should see an improvement in the number of iterations.

    ```
    $ java RunPathFinder ucs GridGraph graphs/GridGraph/AR0012SR.map "11:73" "85:127"
    Loop iterations: 40266
    Elapsed time: 0.035 seconds
    Total cost from 11:73 -> 85:127: 147.68124086713186
    Total path length: 123
    Path: 11:73 -> 12:73 -> 13:73 -> 14:73 -> 15:74 -> ... -> 89:123 -> 88:124 -> 87:125 -> 86:126 -> 85:127
    
    $ java RunPathFinder astar GridGraph graphs/GridGraph/AR0012SR.map "11:73" "85:127"
    Loop iterations: 16700
    Elapsed time: 0.023 seconds
    Total cost from 11:73 -> 85:127: 147.68124086713186
    Total path length: 123
    Path: 11:73 -> 12:74 -> 13:74 -> 14:75 -> 15:76 -> ... -> 87:123 -> 86:124 -> 86:125 -> 85:126 -> 85:127
    ``` 

- **WordLadder**: The number of character positions where the letters differ from each other. This means that `guessCost("örter", "arten")` should be 2, since the first and last characters differ (`ö`/`a` and `r`/`n`), but the middle ones (`rte`) are the same.

    ```
    $ java RunPathFinder ucs WordLadder graphs/WordLadder/words-saldo.txt eller glada
    Loop iterations: 25481
    Elapsed time: 0.387 seconds
    Total cost from eller -> glada: 7.0
    Total path length: 8
    Path: eller -> elles -> ellas -> elias -> glias -> glids -> glads -> glada
    
    $ java RunPathFinder astar WordLadder graphs/WordLadder/words-saldo.txt eller glada
    Loop iterations: 192
    Elapsed time: 0.054 seconds
    Total cost from eller -> glada: 7.0
    Total path length: 8
    Path: eller -> elles -> ellas -> elias -> glias -> glids -> glads -> glada
    ```

- You don't have to implement `guessCost` for **AdjacencyGraph**, because you need domain-specific information about the graph, which you don't have. But see the optional tasks below.

### Important note

The A* algorithm is only optimal if the heuristics is *admissible*, which means that the heuristics must never over-estimate the cost. It's fine to under-estimate, or even set the heuristics to 0: It will still find an optimal path, but if the heuristics is too under-estimating, it will take longer time.

Sometimes you can play with this, if you want to find a solution fast but don't care if the path is not optimal. E.g., if you change the **NPuzzle** heuristics to 10 times the estimate (i.e., `return 10*diff`), then you get the following:

```
$ java RunPathFinder astar NPuzzle 3 "/CBA/DEF/_HG/" "/ABC/DEF/GH_/"
Loop iterations: 743
Elapsed time: 0.066 seconds
Total cost from /CBA/DEF/_HG/ -> /ABC/DEF/GH_/: 46.0
Total path length: 47
Path: /CBA/DEF/_HG/ -> /CBA/DEF/H_G/ -> /CBA/DEF/HG_/ -> /CBA/DE_/HGF/ -> /CB_/DEA/HGF/ -> ... -> /AC_/DBF/GEH/ -> /A_C/DBF/GEH/ -> /ABC/D_F/GEH/ -> /ABC/DEF/G_H/ -> /ABC/DEF/GH_/
```

As you can see, the found solution is much more "expensive" (46.0) than the optimal solution (which is 24.0 as can be seen above). But on the other hand, the number of iterations (743) are one tenth of the optimal search (7461).


## Your submission

You should submit the following files:

- **PathFinder.java**
- **WordLadder.java**
- **GridGraph.java**
- **answers.txt**, with all (non-optional) questions answered

***Do not*** change the file names! ***Do not*** declare them as part of a package! And ***do not*** change any existing code outside the `/* TODO */` parts, because this will make our test scripts throw in the towel. (But you are allowed to add things, such as import statements, new instance variables, and new private methods). 

***Note***: if you do a resubmission, Canvas will automatically rename your files to PathFinder-1.java, answers-66.txt etc. This is how it should be and nothing to worry about.


## Optional tasks

This lab can be expanded in several ways, here are only some suggestions:

- Try the implementations on more graphs: There are several to download from [Moving AI Lab](https://www.movingai.com/benchmarks/grids.html), or [the SNAP project](http://snap.stanford.edu/data/index.html). You can also create more random mazes from several places on the web (search for "random maze generator").

- Show the results nicer, e.g., as an image (for **GridGraph**) or as an animation (for **NPuzzle**).

- **WordLadder** assumes that the start and goal have the same length. Invent and implement word ladder rules for changing the number of letters in a word. Remember that all graph nodes must always be words according to the dicitionary.

- You can assign different costs for different kinds of "terrain" (i.e., different characters) in **GridGraph**. 

- Try to come up with an even better heuristics than straight-line distance for **GridGraph**, remembering that you are not allowed to guess a too high value. Hint: Modify the [Manhattan distance](https://en.wikipedia.org/wiki/Taxicab_geometry) so that it allows for diagonal moves.

- Implement code for reading other graph formats, e.g., for using an image directly as a graph, where dark pixels are considered obstacles. 

- Implement a heuristics for roadmaps (**citygraph-XX.txt**). For this you need locations of all cities, and they can be found in the [DSpace-CRIS database](https://dspace-cris.4science.it/handle/123456789/31). You have to scrape the information you need from the database, i.e., the latitude-longitude of each city. Then you have to filter the database to only include the cities you want. You also need to read in the position database in the graph class, and finally you need to [translate latitude-longitude into kilometers](https://stackoverflow.com/questions/27928/calculate-distance-between-two-latitude-longitude-points-haversine-formula).

- What kind of heuristics could be useful for the link distance between two Wikipedia pages (remember **wikipedia-graph.txt**)? Assume e.g. that you know the text content of both pages. Or that you know the [categories](https://en.wikipedia.org/wiki/Help:Category) that each Wikipedia page belongs to.
