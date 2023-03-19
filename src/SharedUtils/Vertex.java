package SharedUtils;

import java.util.ArrayList;

/**
 * The Vertex class is used to represent a vertex in a graph.
 * Each vertex has a `name`, an `value` and is connected to other vertices through its `edges`.
 * The `successors` of the vertex are the vertices that are not in `edges` and are different from the current vertex.
 */
public class Vertex {

    /** The name of the vertex */
    private String name;

    /** The value of the vertex */
    private int value;

    /** The list of vertices that are connected to this vertex through an edge */
    private ArrayList<Vertex> edges;

    /** The list of vertices that are not connected to this vertex and are not the current vertex */
    private ArrayList<Vertex> successors;

    /**
     * Creates a new vertex with the given `name` and `value`
     * @param name The name of the vertex
     * @param value The value of the vertex
     */
    public Vertex(String name, int value) {
        this.name = name;
        this.value = value;
        this.edges = new ArrayList<>();
        this.successors = new ArrayList<>();
    }

    /**
     * Returns the name of the vertex
     * @return The name of the vertex
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the value of the vertex
     * @return The value of the vertex
     */
    public int getValue() {
        return value;
    }

    /**
     * Returns the list of vertices that are connected to this vertex through an edge
     * @return The list of vertices that are connected to this vertex through an edge
     */
    public ArrayList<Vertex> getEdges() {
        return edges;
    }

    /**
     * Adds a new edge to this vertex connecting it to another vertex
     * @param vertex The vertex that this vertex should be connected to through an edge
     */
    public void addEdge(Vertex vertex) {
        edges.add(vertex);
    }

    /**
     * Sets the successors of this vertex
     * @param successors The vertices that are not in `edges` and are different from the current vertex
     */
    public void setSuccessors(ArrayList<Vertex> successors) {
        this.successors = successors;
    }

    /**
     * Returns the successors of this vertex
     * @return The vertices that are not in `edges` and are different from the current vertex
     */
    public ArrayList<Vertex> getSuccessors() {
        return successors;
    }

    /**
     * Returns a string representation of the vertex.
     * The string representation includes the name of the vertex.
     * @return A string representation of the vertex
     */
    @Override
    public String toString() {
        return name;
    }

}
