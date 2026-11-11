package com.iekatsatos.algorithms.graph

import com.iekatsatos.algorithms.graph.representation.DirectedAdjacencyListGraph
import com.iekatsatos.algorithms.graph.representation.UndirectedAdjacencyListGraph

/**
 * A mutable builder that collects vertices and edges and then creates a [DirectedGraph] or an [UndirectedGraph]
 * from them.
 *
 * Vertices and edges are the same for both kinds of graph; only their interpretation differs. The builder therefore
 * collects them once, and the kind of graph is chosen by the terminal method: [buildDirected] or [buildUndirected].
 *
 * Adding an edge also adds its two endpoints as vertices, so [addVertex] is only needed for isolated vertices.
 * Vertices and edges are kept in insertion order, and duplicates are ignored.
 *
 * The data structure that backs the created graph is chosen by the [GraphRepresentation] argument of the terminal
 * methods, which is an adjacency list by default. The builder prepares the input (copies, ordering, normalization)
 * and the representation only decides how it is stored. The concrete graph classes are not visible to callers, who
 * only work with the [DirectedGraph] and [UndirectedGraph] interfaces.
 *
 * The builder is not thread-safe. The terminal methods copy its content, so a builder can be reused after building,
 * and graphs that were already built are not affected by later changes to it.
 *
 * @param T the vertex type
 */
class GraphBuilder<T> {

    private val vertices: MutableSet<T> = linkedSetOf()
    private val edges: MutableSet<Edge<T>> = linkedSetOf()

    /**
     * Adds [vertex] to the graph. Adding an existing vertex has no effect.
     *
     * @param vertex the vertex to add
     * @return this builder
     */
    fun addVertex(vertex: T): GraphBuilder<T> = apply { vertices.add(vertex) }

    /**
     * Adds all the given [vertices] to the graph. Vertices that are already present are ignored.
     *
     * @param vertices the vertices to add
     * @return this builder
     */
    fun addVertices(vararg vertices: T): GraphBuilder<T> = apply { vertices.forEach(::addVertex) }

    /**
     * Adds an edge from [u] to [v], and adds [u] and [v] as vertices if they are not yet present.
     *
     * In a directed graph, [u] is the source and [v] the target. In an undirected graph the orientation carries no
     * meaning. Adding an existing edge has no effect.
     *
     * @param u the source vertex (directed graphs) or one endpoint (undirected graphs)
     * @param v the target vertex (directed graphs) or the other endpoint (undirected graphs)
     * @return this builder
     */
    fun addEdge(u: T, v: T): GraphBuilder<T> = apply {
        vertices.add(u)
        vertices.add(v)
        edges.add(Edge(u, v))
    }

    /**
     * Adds the given [edge], and its endpoints as vertices if they are not yet present.
     *
     * @param edge the edge to add
     * @return this builder
     */
    fun addEdge(edge: Edge<T>): GraphBuilder<T> = addEdge(edge.u, edge.v)

    /**
     * Adds all the given [edges], and their endpoints as vertices if they are not yet present.
     *
     * @param edges the edges to add
     * @return this builder
     */
    fun addEdges(vararg edges: Edge<T>): GraphBuilder<T> = apply { edges.forEach(::addEdge) }

    /**
     * Creates a [DirectedGraph] from the collected vertices and edges.
     *
     * Every edge `(u, v)` is directed from `u` to `v`, so `Edge(a, b)` and `Edge(b, a)` are distinct edges and both
     * are kept. The created graph receives copies of the collected vertices and edges, both in insertion order, so
     * later changes to the builder do not affect it.
     *
     * @param representation the data structure that backs the graph, an adjacency list by default
     * @return a new directed graph backed by the given [representation]
     */
    fun buildDirected(
        representation: GraphRepresentation = GraphRepresentation.ADJACENCY_LIST
    ): DirectedGraph<T> {
        val verticesCopy = LinkedHashSet(vertices)
        val edgesCopy = LinkedHashSet(edges)
        return when (representation) {
            GraphRepresentation.ADJACENCY_LIST -> DirectedAdjacencyListGraph(verticesCopy, edgesCopy)
        }
    }

    /**
     * Creates an [UndirectedGraph] from the collected vertices and edges.
     *
     * Edges have no direction, so `Edge(a, b)` and `Edge(b, a)` describe the same connection. The edges are
     * therefore normalized: only the orientation that was added first is kept. A self-loop `(v, v)` is kept once.
     * The created graph receives copies of the collected vertices and the normalized edges, both in insertion order,
     * so later changes to the builder do not affect it.
     *
     * @param representation the data structure that backs the graph, an adjacency list by default
     * @return a new undirected graph backed by the given [representation]
     */
    fun buildUndirected(
        representation: GraphRepresentation = GraphRepresentation.ADJACENCY_LIST
    ): UndirectedGraph<T> {
        val verticesCopy = LinkedHashSet(vertices)
        val edgesCopy = normalizedEdges()
        return when (representation) {
            GraphRepresentation.ADJACENCY_LIST -> UndirectedAdjacencyListGraph(verticesCopy, edgesCopy)
        }
    }

    /**
     * Returns the collected edges with at most one orientation per connection, keeping the one added first.
     */
    private fun normalizedEdges(): MutableSet<Edge<T>> {
        val normalized: MutableSet<Edge<T>> = linkedSetOf()
        for (edge in edges) {
            if (Edge(edge.v, edge.u) !in normalized) normalized.add(edge)
        }
        return normalized
    }
}