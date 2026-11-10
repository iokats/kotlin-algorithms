package com.iekatsatos.algorithms.graph

/**
 * Represents an edge connecting the vertices [u] and [v].
 *
 * In a [DirectedGraph], [u] is the source and [v] is the target. In an [UndirectedGraph], the orientation carries no
 * meaning: `Edge(a, b)` and `Edge(b, a)` describe the same connection, even though, as a data class, they are not equal.
 * Undirected implementations should therefore treat them as one connection.
 *
 * @param T the vertex type
 * @property u the source vertex (directed graphs) or one endpoint (undirected graphs)
 * @property v the target vertex (directed graphs) or the other endpoint (undirected graphs)
 */
data class Edge<T>(val u:T, val v:T)
