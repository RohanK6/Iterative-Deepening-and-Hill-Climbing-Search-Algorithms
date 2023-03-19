package IterativeDeepening;

import SharedUtils.Graph;
import SharedUtils.Vertex;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    private static int TARGET;
    private static boolean IS_VERBOSE;
    private static Graph graph;
    private static ArrayList<String> VERBOSE_OUTPUTS;
    private static String solution;

    public static void main(String[] args) throws FileNotFoundException, URISyntaxException {
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

        /* For each vertex, we want a list of vertices that are not connected to it by an edge */
        graph.iterateAndSetSuccessors();

        /* Now the file is fully read, the Graph is constructed with Vertices and Edges */
        iterativeDeepening();
        if (IS_VERBOSE) {
            for (String v : VERBOSE_OUTPUTS) {
                System.out.println(v);
            }
        }
        System.out.println(solution);
    }

    public static void iterativeDeepening() {
        for (int depth = 1; depth < graph.getVertices().size()+1; depth++) {
            /* VERBOSE_OUTPUTS: Display the current depth */
            VERBOSE_OUTPUTS.add("\n");
            VERBOSE_OUTPUTS.add("Depth=" + depth);

            /* We want all the vertices to iterate through */
            ArrayList<Vertex> vertices = graph.getVertices();
            for (Vertex vertex : vertices) {
                int iteratingTarget = 0;
                /* The traversal path for the DFS algorithm */
                ArrayList<Vertex> traversalPath = new ArrayList<Vertex>();
                /* Run DFS */
                int dfsAnswer = dfs(depth, vertex, traversalPath, iteratingTarget);

                /* Answer found! We can print and return */
                if (dfsAnswer == TARGET) {
                    solution += "Found solution ";
                    for (int i = 0; i < traversalPath.size(); i++) {
                        solution += traversalPath.get(i) + " ";
                    }
                    solution += "Value=" + TARGET;
                    return;
                }
            }
        }
        /* No answer was found, and the search has failed */
        solution += "Search failed";
    }

    public static int dfs(int depth, Vertex currentVertex, ArrayList<Vertex> traversalPath, int iteratingTarget) {
        if (depth == 0) return iteratingTarget;

        traversalPath.add(currentVertex);

        /* VERBOSE_OUTPUTS: Add the current vertex and the total value in traversalPath */
        int currentTotalValue = traversalPath.stream().mapToInt(vertex -> vertex.getValue()).sum();
        String currentVisitedVertices = String.join(" ", traversalPath.stream().map(vertex -> vertex.getName()).toArray(String[]::new));
        VERBOSE_OUTPUTS.add(currentVisitedVertices + " Value=" + currentTotalValue);

        /* Set the target we have from iterating to the currentTotalValue */
        iteratingTarget = currentTotalValue;

        ArrayList<Vertex> successors = currentVertex.getSuccessors();
        /* Get the successors that are still available to add for the current vertex */
        ArrayList<Vertex> successorsAvailable = new ArrayList<>();
        for (Vertex vertex : successors) {
            if (!traversalPath.contains(vertex)) {
                successorsAvailable.add(vertex);
            }
        }

        /* Check all successors that are available and unvisited */
        for (Vertex successor : successorsAvailable) {
            int result = dfs(depth - 1, successor, traversalPath, iteratingTarget);
            if (result == TARGET) {
                return result;
            }
        }
        /* Check if we've reached the TARGET on the current iteration */
        if (iteratingTarget == TARGET) {
            return iteratingTarget;
        }
        /* If we have not reached the target and have nowhere to go, we pop the most recent vertex */
        traversalPath.remove(traversalPath.size() - 1);

        return iteratingTarget;
    }

}
