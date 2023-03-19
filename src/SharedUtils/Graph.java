/**
 * Graph class represents a directed graph and its operations.
 * The graph contains a list of vertices.
 */
package SharedUtils;

import java.util.ArrayList;

public class Graph {

    /**
     * List of vertices in the graph.
     */
    private ArrayList<Vertex> vertices;

    /**
     * Constructs an empty graph.
     */
    public Graph() {
        this.vertices = new ArrayList<>();
    }

    /**
     * Returns the list of vertices in the graph.
     *
     * @return the list of vertices in the graph.
     */
    public ArrayList<Vertex> getVertices() {
        return vertices;
    }

    /**
     * Returns the vertex with the specified name in the graph.
     * If the vertex is not found, returns null.
     *
     * @param name the name of the vertex to be retrieved.
     * @return the vertex with the specified name in the graph, or null if it doesn't exist.
     */
    public Vertex getVertexFromGraph(String name) {
        for (Vertex v : vertices) {
            if (v.getName().equals(name)) {
                return v;
            }
        }
        return null;
    }

    /**
     * Adds a vertex to the graph.
     *
     * @param vertex the vertex to be added to the graph.
     */
    public void addVertex(Vertex vertex) {
        vertices.add(vertex);
    }

    /**
     * Iterates through each vertex in the graph, and sets its successors based on the edges (not connected).
     * A valid successor is defined in Homework 1 as not connected via an edge AND
     * alphabetically follows the vertex (so A cannot be a successor to B, but B can be
     * successor to A). This will only be useful for the Iterative Deepening algorithm.
     */
    public void iterateAndSetSuccessors() {
        for (Vertex vertex : vertices) {
            ArrayList<Vertex> successors = new ArrayList<>();
            for (Vertex iteratingVertex : vertices) {
                boolean isSuccessor = true;
                for (Vertex v : vertex.getEdges()) {
                    if (v.getName().equals(iteratingVertex.getName())) {
                        isSuccessor = false;
                        break;
                    }
                }
                if (isSuccessor && !vertex.getName().equals(iteratingVertex.getName()) && vertex.getName().compareTo(iteratingVertex.getName()) < 0) {
                    successors.add(iteratingVertex);
                }
            }
            vertex.setSuccessors(successors);
        }
    }


    /**
     * Returns a string representation of the graph.
     *
     * @return a string representation of the graph.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Vertex vertex : vertices) {
            sb.append(vertex.toString());
            sb.append("\n");
        }
        return sb.toString();
    }
}