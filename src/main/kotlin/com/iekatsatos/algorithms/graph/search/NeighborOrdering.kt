package com.iekatsatos.algorithms.graph.search

import com.iekatsatos.algorithms.graph.Graph

/**
 * Returns the neighbors of [vertex], sorted by [comparator] when one is given,
 * otherwise in the graph's own iteration order.
 */
internal fun <T> Graph<T>.orderedNeighborsOf(vertex: T, comparator: Comparator<in T>?): List<T> =
    if (comparator != null) neighborsOf(vertex, comparator) else neighborsOf(vertex).toList()