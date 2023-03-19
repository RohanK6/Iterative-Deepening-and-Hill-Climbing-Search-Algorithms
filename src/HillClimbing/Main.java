package HillClimbing;

import SharedUtils.Graph;
import SharedUtils.Vertex;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

public class Main {
    private static int TARGET;
    private static boolean IS_VERBOSE;
    private static int RESTARTS;
    private static Graph graph;
    private static ArrayList<String> VERBOSE_OUTPUTS;
    private static String solution;

    public static void main(String[] args) throws FileNotFoundException, URISyntaxException{
        /* Initialization */
        URL url = Main.class.getResource("input.txt");
        Path path = Paths.get(url.toURI());
        File file = path.toFile();
        Scanner in = new Scanner(file);
        graph = new Graph();
        VERBOSE_OUTPUTS = new ArrayList<String>();
        solution = "\n";

        /* Read the first line for the important information */
        String algoLine = in.nextLine();
        String[] algoInfo = algoLine.trim().split(" ");
        TARGET = Integer.parseInt(algoInfo[0]);
        IS_VERBOSE = algoInfo[1].equals("V");
        RESTARTS = Integer.parseInt(algoInfo[2]);

        /* Loop through the vertices and their equivalent value, stop when the scanner reaches an empty line */
        while (in.hasNextLine()) {
            String line = in.nextLine().trim();
            if (line.isEmpty()) {
                break;
            }

            String[] vertexInfo = line.split(" ");
            String vertexName = vertexInfo[0];
            int vertexValue = Integer.parseInt(vertexInfo[1]);
            Vertex vertex = new Vertex(vertexName, vertexValue);
            graph.addVertex(vertex);
        }

        /* Now loop through the edges and stop when the scanner reaches the end of the file */
        while (in.hasNextLine()) {
            String line = in.nextLine().trim();
            if (line.isEmpty()) {
                break;
            }

            String[] edgeInfo = line.split(" ");
            String vertexOneName = edgeInfo[0];
            Vertex vertexOne = graph.getVertexFromGraph(vertexOneName);
            String vertexTwoName = edgeInfo[1];
            Vertex vertexTwo = graph.getVertexFromGraph(vertexTwoName);

            vertexOne.addEdge(vertexTwo);
            vertexTwo.addEdge(vertexOne);
        }

        /* Now the file is fully read, the Graph is constructed with Vertices and Edges */
        hillClimbing();

        /* Print according to IS_VERBOSE */
        if (IS_VERBOSE) {
            for (String verboseOutput : VERBOSE_OUTPUTS) {
                System.out.println(verboseOutput);
            }
        }
        System.out.println(solution);
    }

    public static ArrayList<Vertex> getRandomStartState() {
        /* A random starting point is constructed by going through the list of vertices and accepting or rejecting each with probability 1/2 */
        ArrayList<Vertex> startState = new ArrayList<>();
        for (Vertex vertex : graph.getVertices()) {
            if (Math.random() < 0.5) {
                startState.add(vertex);
            }
        }
        return startState;
    }

     public static ArrayList<Vertex> getNeighborsAndUpdatePath(ArrayList<Vertex> currentPath) {
        ArrayList<Vertex> neighbors = new ArrayList<>();
        ArrayList<Vertex> bestPath = new ArrayList<>(currentPath);

        /* Add vertices that are not already in the current path */
        for (Vertex vertex : graph.getVertices()) {
            boolean isInCurrentPath = false;
            for (Vertex currentPathVertex : currentPath) {
                /* We check by name, not object equality */
                if (vertex.getName().equals(currentPathVertex.getName())) {
                    isInCurrentPath = true;
                    break;
                }
            }

             if (!isInCurrentPath) {
                neighbors.add(vertex);
            }
        }

        VERBOSE_OUTPUTS.add("Neighbors");
        /* Check scenarios by removing an existing vertex */
        for (Vertex vertex : currentPath) {
            ArrayList<Vertex> newPath = new ArrayList<>(currentPath);
            newPath.remove(vertex);
            int error = errorFunction(newPath);
            newPath.sort((v1, v2) -> v1.getName().compareTo(v2.getName()));
            VERBOSE_OUTPUTS.add(stringFunction(newPath) + " Value=" + valueFunction(newPath) + ". Error=" + error + ".");
            /* Lower error found */
            if (error < errorFunction(bestPath)) {
                bestPath = newPath;
            }
            /* Error = 0 so solution is found. Return early */
            if (error == 0) return bestPath;
        }
        /* Check scenarios by adding a neighbor */
        for (Vertex vertex : neighbors) {
            ArrayList<Vertex> newPath = new ArrayList<>(currentPath);
            newPath.add(vertex);
            int error = errorFunction(newPath);
            newPath.sort((v1, v2) -> v1.getName().compareTo(v2.getName()));
            VERBOSE_OUTPUTS.add(stringFunction(newPath) + " Value=" + valueFunction(newPath) + ". Error=" + error + ".");
            /* Lower error found */
            if (error < errorFunction(bestPath)) {
                bestPath = newPath;
            }
            /* Error = 0 so solution is found. Return early */
            if (error == 0) return bestPath;
        }

        return bestPath;
    }    

    public static void hillClimbing() {
        /* Hill climb for the specified number of RESTARTS */
        for (int i = 0; i < RESTARTS; i++) {
            /* Initialize the starting state */
            int globalMinimumError = Integer.MAX_VALUE;
            ArrayList<Vertex> startState = getRandomStartState();
            String startStateVertices = stringFunction(startState);
            VERBOSE_OUTPUTS.add("Randomly chosen start state: " + startStateVertices);
            VERBOSE_OUTPUTS.add(stringFunction(startState) + " Value=" + valueFunction(startState) + ". Error=" + errorFunction(startState) + ".");
            ArrayList<Vertex> currentPath = startState;

            /* Infinite loop to hill climb */
            while (true) {
                /* Get the neighbors and find the lowest value path  */
                currentPath = getNeighborsAndUpdatePath(currentPath);
                VERBOSE_OUTPUTS.add("\n");
                /* If the error of the best possible path increases the error, we fail the search (based on the examples provided) */
                if (errorFunction(currentPath) >= globalMinimumError) {
                    solution = "Search failed.\n";
                    /* If we're on the last RESTART, just return from here, since we already set solution anyways */
                    if (i == RESTARTS - 1) return;
                    break;
                } else if (errorFunction(currentPath) == 0) {
                    solution = "Found solution " + stringFunction(currentPath) + " Value=" + valueFunction(currentPath);
                    return;
                } else {
                    VERBOSE_OUTPUTS.add("Move to " + stringFunction(currentPath) + " Value=" + valueFunction(currentPath) + ". Error=" + errorFunction(currentPath) + ".");
                    globalMinimumError = errorFunction(currentPath);
                }
            }

            VERBOSE_OUTPUTS.add(solution);
        } 
    }

    public static String stringFunction(ArrayList<Vertex> path) {
        if (path.size() == 0) return "{}";
        return String.join(" ", path.stream().map(vertex -> vertex.getName()).toArray(String[]::new));
    }

    public static int valueFunction(ArrayList<Vertex> path) {
        return path.stream().mapToInt(vertex -> vertex.getValue()).sum();
    }

    public static int errorFunction(ArrayList<Vertex> path) {
        /* We want to calculate the error value to determine the best move in the hill climbing algorithm 
         * The formula is: Max(0, TARGET-(Total value of vertices in the path)) + sum of the cost of all edges
         * that connect two vertices in the path. The cost of an edge is considered to be the value of its lower end.
         * For example, if the edge A (5) and B (7) are connected, the cost of the edge is 5.
        */
        int errorValue = 0;
        int totalValue = 0;
        for (Vertex vertex : path) {
            totalValue += vertex.getValue();
        }
        errorValue = Math.max(0, TARGET - totalValue);

        HashSet<String> processedEdges = new HashSet<>();
        for (Vertex vertex : path) {
            /* For each vertex, we want to check if it has any edges that connect to another vertex in the path.
               We check by vertex name, not by vertex object. So we can't just use the contains() method. */
            for (Vertex edge : vertex.getEdges()) {
                for (Vertex pathVertex : path) {
                    if (edge.getName().equals(pathVertex.getName()) && !edge.getName().equals(vertex.getName())) {
                        /* We compare the names to make sure the edge A-B is considered the same as B-A, storing as A-B */
                        String edgeName = vertex.getName().compareTo(edge.getName()) < 0 
                                         ? vertex.getName() + "-" + edge.getName() 
                                         : edge.getName() + "-" + vertex.getName();
                        if (!processedEdges.contains(edgeName)) {
                            errorValue += Math.min(vertex.getValue(), edge.getValue());
                            processedEdges.add(edgeName);
                        }
                    }
                }
            }
        }
        return errorValue;
    }
}
