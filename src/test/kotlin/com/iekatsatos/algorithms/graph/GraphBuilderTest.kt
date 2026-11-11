package com.iekatsatos.algorithms.graph

import com.iekatsatos.algorithms.graph.representation.DirectedAdjacencyListGraph
import com.iekatsatos.algorithms.graph.representation.UndirectedAdjacencyListGraph
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame
import kotlin.test.assertTrue

class GraphBuilderTest {

    @Test
    fun directed_whenNothingAdded_thenEmptyGraph() {

        // given
        val builder = GraphBuilder<Int>()

        // when
        val graph = builder.buildDirected()

        // then
        assertTrue(graph.vertices.isEmpty())
        assertTrue(graph.edges.isEmpty())
    }

    @Test
    fun undirected_whenNothingAdded_thenEmptyGraph() {

        // given
        val builder = GraphBuilder<Int>()

        // when
        val graph = builder.buildUndirected()

        // then
        assertTrue(graph.vertices.isEmpty())
        assertTrue(graph.edges.isEmpty())
    }

    @Test
    fun addMethods_whenCalled_thenReturnTheSameBuilder() {

        // given
        val builder = GraphBuilder<Int>()

        // when / then
        assertSame(builder, builder.addVertex(1))
        assertSame(builder, builder.addVertices(2, 3))
        assertSame(builder, builder.addEdge(1, 2))
        assertSame(builder, builder.addEdge(Edge(2, 3)))
        assertSame(builder, builder.addEdges(Edge(3, 1)))
    }

    @Test
    fun addVertex_whenVertexAdded_thenIsolatedVertexIsInTheGraph() {

        // given
        val builder = GraphBuilder<Int>().addVertex(1)

        // when
        val graph = builder.buildDirected()

        // then
        assertEquals(setOf(1), graph.vertices)
        assertTrue(graph.edges.isEmpty())
        assertTrue(graph.neighborsOf(1).isEmpty())
    }

    @Test
    fun addVertex_whenSameVertexAddedTwice_thenItAppearsOnce() {

        // given
        val builder = GraphBuilder<Int>().addVertex(1).addVertex(1)

        // when
        val graph = builder.buildDirected()

        // then
        assertEquals(listOf(1), graph.vertices.toList())
    }

    @Test
    fun addVertices_whenSeveralGiven_thenAllAreAddedAndDuplicatesIgnored() {

        // given
        val builder = GraphBuilder<Int>().addVertices(1, 2, 3, 2, 1)

        // when
        val graph = builder.buildUndirected()

        // then
        assertEquals(setOf(1, 2, 3), graph.vertices)
    }

    @Test
    fun addVertices_whenNoneGiven_thenGraphStaysEmpty() {

        // given
        val builder = GraphBuilder<Int>().addVertices()

        // when
        val graph = builder.buildDirected()

        // then
        assertTrue(graph.vertices.isEmpty())
    }

    @Test
    fun addEdge_whenEdgeAdded_thenBothEndpointsBecomeVertices() {

        // given
        val builder = GraphBuilder<Int>().addEdge(1, 2)

        // when
        val graph = builder.buildDirected()

        // then
        assertEquals(setOf(1, 2), graph.vertices)
        assertEquals(setOf(Edge(1, 2)), graph.edges)
    }

    @Test
    fun addEdge_whenEndpointAlreadyAddedAsVertex_thenItIsNotDuplicated() {

        // given
        val builder = GraphBuilder<Int>().addVertex(1).addEdge(1, 2)

        // when
        val graph = builder.buildDirected()

        // then
        assertEquals(listOf(1, 2), graph.vertices.toList())
    }

    @Test
    fun addEdge_whenSameEdgeAddedTwice_thenItAppearsOnce() {

        // given
        val builder = GraphBuilder<Int>().addEdge(1, 2).addEdge(1, 2)

        // when
        val graph = builder.buildDirected()

        // then
        assertEquals(1, graph.edges.size)
    }

    @Test
    fun addEdge_whenEdgeObjectGiven_thenSameAsAddingItsEndpoints() {

        // given
        val builder = GraphBuilder<Int>().addEdge(Edge(1, 2))

        // when
        val graph = builder.buildDirected()

        // then
        assertEquals(setOf(1, 2), graph.vertices)
        assertEquals(setOf(Edge(1, 2)), graph.edges)
    }

    @Test
    fun addEdges_whenSeveralGiven_thenAllEdgesAndTheirEndpointsAreAdded() {

        // given
        val builder = GraphBuilder<Int>().addEdges(Edge(1, 2), Edge(2, 3), Edge(2, 3))

        // when
        val graph = builder.buildDirected()

        // then
        assertEquals(setOf(1, 2, 3), graph.vertices)
        assertEquals(setOf(Edge(1, 2), Edge(2, 3)), graph.edges)
    }

    @Test
    fun addEdge_whenSelfLoop_thenVertexIsAddedOnceWithItsLoop() {

        // given
        val builder = GraphBuilder<Int>().addEdge(1, 1)

        // when
        val graph = builder.buildDirected()

        // then
        assertEquals(setOf(1), graph.vertices)
        assertEquals(setOf(Edge(1, 1)), graph.edges)
    }

    @Test
    fun directed_whenVerticesAdded_thenInsertionOrderIsPreserved() {

        // given
        val builder = GraphBuilder<Int>()
            .addVertex(5)
            .addEdge(3, 1)
            .addVertex(4)
            .addEdge(2, 5)

        // when
        val graph = builder.buildDirected()

        // then
        assertEquals(listOf(5, 3, 1, 4, 2), graph.vertices.toList())
    }

    @Test
    fun directed_whenEdgesAdded_thenInsertionOrderIsPreserved() {

        // given
        val builder = GraphBuilder<Int>().addEdge(3, 1).addEdge(2, 3).addEdge(1, 2)

        // when
        val graph = builder.buildDirected()

        // then
        assertEquals(listOf(Edge(3, 1), Edge(2, 3), Edge(1, 2)), graph.edges.toList())
    }

    @Test
    fun directed_whenEdgeAdded_thenOnlyTheSourceSeesTheTarget() {

        // given
        val builder = GraphBuilder<Int>().addEdge(1, 2)

        // when
        val graph = builder.buildDirected()

        // then
        assertEquals(setOf(2), graph.neighborsOf(1))
        assertTrue(graph.neighborsOf(2).isEmpty())
        assertEquals(setOf(1), graph.predecessorsOf(2))
        assertTrue(graph.predecessorsOf(1).isEmpty())
    }

    @Test
    fun directed_whenBothOrientationsAdded_thenBothEdgesAreKept() {

        // given
        val builder = GraphBuilder<Int>().addEdge(1, 2).addEdge(2, 1)

        // when
        val graph = builder.buildDirected()

        // then
        assertEquals(setOf(Edge(1, 2), Edge(2, 1)), graph.edges)
        assertEquals(setOf(2), graph.neighborsOf(1))
        assertEquals(setOf(1), graph.neighborsOf(2))
    }

    @Test
    fun directed_whenDefaultRepresentation_thenAdjacencyList() {

        // given
        val builder = GraphBuilder<Int>().addEdge(1, 2)

        // when
        val graph = builder.buildDirected()

        // then
        assertTrue(graph is DirectedAdjacencyListGraph<Int>)
    }

    @Test
    fun directed_whenAdjacencyListRepresentationRequested_thenAdjacencyList() {

        // given
        val builder = GraphBuilder<Int>().addEdge(1, 2)

        // when
        val graph = builder.buildDirected(GraphRepresentation.ADJACENCY_LIST)

        // then
        assertTrue(graph is DirectedAdjacencyListGraph<Int>)
    }

    @Test
    fun undirected_whenEdgeAdded_thenBothEndpointsSeeEachOther() {

        // given
        val builder = GraphBuilder<Int>().addEdge(1, 2)

        // when
        val graph = builder.buildUndirected()

        // then
        assertEquals(setOf(2), graph.neighborsOf(1))
        assertEquals(setOf(1), graph.neighborsOf(2))
    }

    @Test
    fun undirected_whenBothOrientationsAdded_thenOnlyTheFirstIsKept() {

        // given
        val builder = GraphBuilder<Int>().addEdge(1, 2).addEdge(2, 1)

        // when
        val graph = builder.buildUndirected()

        // then
        assertEquals(setOf(Edge(1, 2)), graph.edges)
    }

    @Test
    fun undirected_whenReverseOrientationAddedFirst_thenThatOrientationIsKept() {

        // given
        val builder = GraphBuilder<Int>().addEdge(2, 1).addEdge(1, 2)

        // when
        val graph = builder.buildUndirected()

        // then
        assertEquals(setOf(Edge(2, 1)), graph.edges)
    }

    @Test
    fun undirected_whenSelfLoop_thenLoopIsKeptOnce() {

        // given
        val builder = GraphBuilder<Int>().addEdge(1, 1).addEdge(1, 1)

        // when
        val graph = builder.buildUndirected()

        // then
        assertEquals(setOf(Edge(1, 1)), graph.edges)
        assertEquals(setOf(1), graph.neighborsOf(1))
    }

    @Test
    fun undirected_whenMixedEdges_thenOnlyDuplicateConnectionsAreRemoved() {

        // given
        val builder = GraphBuilder<Int>()
            .addEdge(1, 2)
            .addEdge(2, 3)
            .addEdge(3, 2)
            .addEdge(3, 1)

        // when
        val graph = builder.buildUndirected()

        // then
        assertEquals(listOf(Edge(1, 2), Edge(2, 3), Edge(3, 1)), graph.edges.toList())
    }

    @Test
    fun undirected_whenDefaultRepresentation_thenAdjacencyList() {

        // given
        val builder = GraphBuilder<Int>().addEdge(1, 2)

        // when
        val graph = builder.buildUndirected()

        // then
        assertTrue(graph is UndirectedAdjacencyListGraph<Int>)
    }

    @Test
    fun undirected_whenAdjacencyListRepresentationRequested_thenAdjacencyList() {

        // given
        val builder = GraphBuilder<Int>().addEdge(1, 2)

        // when
        val graph = builder.buildUndirected(GraphRepresentation.ADJACENCY_LIST)

        // then
        assertTrue(graph is UndirectedAdjacencyListGraph<Int>)
    }

    @Test
    fun directedAndUndirected_whenBuiltFromTheSameBuilder_thenShareTheSameVertices() {

        // given
        val builder = GraphBuilder<Char>().addEdge('a', 'b').addEdge('b', 'c').addVertex('z')

        // when
        val directed = builder.buildDirected()
        val undirected = builder.buildUndirected()

        // then
        assertEquals(directed.vertices, undirected.vertices)
        assertEquals(listOf('a', 'b', 'c', 'z'), directed.vertices.toList())
    }

    @Test
    fun directed_whenBuiltTwice_thenGraphsAreEqual() {

        // given
        val builder = GraphBuilder<Int>().addEdge(1, 2).addEdge(2, 3)

        // when
        val first = builder.buildDirected()
        val second = builder.buildDirected()

        // then
        assertEquals(first, second)
    }

    @Test
    fun undirected_whenBuiltTwice_thenGraphsAreEqual() {

        // given
        val builder = GraphBuilder<Int>().addEdge(1, 2).addEdge(2, 3)

        // when
        val first = builder.buildUndirected()
        val second = builder.buildUndirected()

        // then
        assertEquals(first, second)
    }

    @Test
    fun directed_whenBuilderChangedAfterBuilding_thenBuiltGraphIsUnchanged() {

        // given
        val builder = GraphBuilder<Int>().addEdge(1, 2)
        val graph = builder.buildDirected()

        // when
        builder.addEdge(2, 3).addVertex(9)

        // then
        assertEquals(setOf(1, 2), graph.vertices)
        assertEquals(setOf(Edge(1, 2)), graph.edges)
    }

    @Test
    fun undirected_whenBuilderChangedAfterBuilding_thenBuiltGraphIsUnchanged() {

        // given
        val builder = GraphBuilder<Int>().addEdge(1, 2)
        val graph = builder.buildUndirected()

        // when
        builder.addEdge(2, 3).addVertex(9)

        // then
        assertEquals(setOf(1, 2), graph.vertices)
        assertEquals(setOf(Edge(1, 2)), graph.edges)
    }

    @Test
    fun directed_whenBuilderReusedAfterBuilding_thenNextGraphContainsEverythingAdded() {

        // given
        val builder = GraphBuilder<Int>().addEdge(1, 2)
        builder.buildDirected()

        // when
        val graph = builder.addEdge(2, 3).buildDirected()

        // then
        assertEquals(setOf(1, 2, 3), graph.vertices)
        assertEquals(setOf(Edge(1, 2), Edge(2, 3)), graph.edges)
    }

    @Test
    fun undirected_whenNormalizing_thenBuilderEdgesAreNotModified() {

        // given: normalizing the undirected edges must not drop anything from the builder
        val builder = GraphBuilder<Int>().addEdge(1, 2).addEdge(2, 1)

        // when
        builder.buildUndirected()
        val directed = builder.buildDirected()

        // then
        assertEquals(setOf(Edge(1, 2), Edge(2, 1)), directed.edges)
    }

    @Test
    fun directed_whenVertexTypeIsNotInt_thenWorksWithAnyHashableType() {

        // given
        val builder = GraphBuilder<String>().addEdge("A", "B").addEdge("B", "C")

        // when
        val graph = builder.buildDirected()

        // then
        assertEquals(setOf("C"), graph.neighborsOf("B"))
        assertEquals(setOf("A"), graph.predecessorsOf("B"))
    }

    @Test
    fun directed_whenVerticesAreDataClasses_thenEqualVerticesAreMerged() {

        // given
        data class City(val name: String)

        val builder = GraphBuilder<City>()
            .addEdge(City("Paris"), City("Rome"))
            .addEdge(City("Paris"), City("Rome"))

        // when
        val graph = builder.buildDirected()

        // then
        assertEquals(setOf(City("Paris"), City("Rome")), graph.vertices)
        assertEquals(1, graph.edges.size)
    }
}