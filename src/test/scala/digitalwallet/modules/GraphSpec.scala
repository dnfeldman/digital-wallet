package digitalwallet.modules

import org.scalatest.{ MustMatchers, WordSpec }

import scala.collection.immutable.IntMap
import scala.collection.mutable.Map
import scala.collection.mutable.Set

/**
  * Created by hriundel on 11/6/16.
  */
class GraphSpec extends WordSpec with MustMatchers {


  /**
    * We will be dealing with the following social network:
    *
    *        1
    *      /  \
    *    2     3
    *         /
    *       4
  */

  "Graph#neighborsOf" when {
    val adjacencyMap = IntMap[Set[Int]](
      1 -> Set(2, 3),
      2 -> Set(1),
      3 -> Set(1, 4),
      4 -> Set(3)
    )

    val graph = new Graph(adjacencyMap)

    "given a node that does not exist in the graph" must {
      "return and empty set" in {
        val nonExistentNode = 999

        graph.neighborsOf(nonExistentNode) must equal(Set[Int]())
      }
    }

    "given a node that exists in the graph" must {
      "return correct neighbors" which {
        "returns Set(2,3) for node 1" in {
          graph.neighborsOf(1) must equal(Set[Int](2, 3))
        }

        "returns Set(1) for node 2" in {
          graph.neighborsOf(2) must equal(Set[Int](1))
        }

        "returns Set(1, 4) for node 3" in {
          graph.neighborsOf(3) must equal(Set[Int](1, 4))
        }

        "returns Set(3) for node 4" in {
          graph.neighborsOf(4) must equal(Set(3))
        }
      }

      "not return nodes that are not directly connected to each other" in {
        graph.neighborsOf(1) mustNot contain(4)
      }
    }
  }

  "Graph#addEdge" must {
    "add new connection if neither of edge nodes are part of the original graph" in {
      val adjacencyMap = IntMap[Set[Int]](
        1 -> Set(2, 3),
        2 -> Set(1),
        3 -> Set(1, 4),
        4 -> Set(3)
      )

      val graph = new Graph(adjacencyMap)


      /**
        *
        *        1                               1     5
        *      /  \        5                   /  \     \
        *    2     3    +   \        =       2     3     6
        *         /          6                     /
        *       4                               4
        */

      val expected = IntMap[Set[Int]](
        1 -> Set(2, 3),
        2 -> Set(1),
        3 -> Set(1, 4),
        4 -> Set(3),
        5 -> Set(6),
        6 -> Set(5)
      )

      graph.addConnection(5, 6)
      graph.adjacencyMap must equal(expected)
    }

    "ignore connection if both of edge nodes are already a part of the original graph" in {
      val adjacencyMap = IntMap[Set[Int]](
        1 -> Set(2, 3),
        2 -> Set(1),
        3 -> Set(1, 4),
        4 -> Set(3)
      )

      val graph = new Graph(adjacencyMap)


      /**
        *
        *        1                               1
        *      /  \        1                   /  \
        *    2     3    +   \        =       2     3
        *         /          3                     /
        *       4                               4
        */

      val expected = IntMap[Set[Int]](
        1 -> Set(2, 3),
        2 -> Set(1),
        3 -> Set(1, 4),
        4 -> Set(3)
      )

      graph.addConnection(1, 3)
      graph.adjacencyMap must equal(expected)
    }

    "correctly update each nodes' adjacencySet" in {
      val adjacencyMap = IntMap[Set[Int]](
        1 -> Set(2, 3),
        2 -> Set(1),
        3 -> Set(1, 4),
        4 -> Set(3)
      )

      val graph = new Graph(adjacencyMap)

      /**
        *
        *        1                               1
        *      /  \        3                   /  \
        *    2     3    +   \        =       2     3
        *         /          5                    / \
        *       4                               4    5
        */

      val expected = IntMap[Set[Int]](
        1 -> Set(2, 3),
        2 -> Set(1),
        3 -> Set(1, 4, 5),
        4 -> Set(3),
        5 -> Set(3)
      )

      graph.addConnection(3, 5)
      graph.adjacencyMap must equal(expected)
    }
  }
}
