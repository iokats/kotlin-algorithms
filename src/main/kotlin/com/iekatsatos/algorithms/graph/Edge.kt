package com.iekatsatos.algorithms.graph

/**
 * Represents an Edge which connects two vertices [u] and [v].
 *
 * @param T the vertex type
 * @property u one endpoint of the edge
 * @property v the other endpoint of the edge
 */
data class Edge<T>(val u:T, val v:T)
