package digitalwallet.modules

import org.scalatest.{ MustMatchers, WordSpec }

import scala.collection.immutable.IntMap
import scala.collection.mutable

class DegreeOfSeparationCheckerSpec extends WordSpec with MustMatchers {

  "DegreeOfSeparationChecker#isWithinNConnections" when {
    /**
      * 1     3     5      8      9
      *  \  /  \  / | \  /         \
      *   2     4   |  7            0
      *    \        |
      *     6 ------
      *
      */

    val adjacencyMap = IntMap[mutable.Set[Int]](
      1 -> mutable.Set(2),
      2 -> mutable.Set(3, 6),
      3 -> mutable.Set(2, 4),
      4 -> mutable.Set(3, 5),
      5 -> mutable.Set(4, 6, 7),
      6 -> mutable.Set(2, 5),
      7 -> mutable.Set(5, 8),
      8 -> mutable.Set(7),
      9 -> mutable.Set(0),
      0 -> mutable.Set(9)
    )
    val graph = new Graph(adjacencyMap)
    val degreeOfSeparationChecker = new DegreeOfSeparationChecker(graph)

    "n = 0" must {
      "return true if both input nodes are the same" in {
        degreeOfSeparationChecker.isWithinNConnections(node1 = 1, node2 = 1, numDegrees = 0) must equal(true)
      }

      "return false if input nodes are not the same" in {
        degreeOfSeparationChecker.isWithinNConnections(node1 = 1, node2 = 2, numDegrees = 0) must equal(false)
      }

      "return false if input nodes are not part of the graph" in {
        degreeOfSeparationChecker.isWithinNConnections(node1 = 100, node2 = 101, numDegrees = 0) must equal(false)

      }
    }

    "n = 1" must {
      "return true if both input nodes are the same" in {
        degreeOfSeparationChecker.isWithinNConnections(node1 = 1, node2 = 1, numDegrees = 1) must equal(true)
      }

      "return true if both input nodes are directly connected to each other" in {
        degreeOfSeparationChecker.isWithinNConnections(node1 = 1, node2 = 2, numDegrees = 1) must equal(true)

      }

      "return false if input nodes are not directly connected" in {
        degreeOfSeparationChecker.isWithinNConnections(node1 = 1, node2 = 3, numDegrees = 1) must equal(false)
      }

      "return false if input nodes are not part of the graph" in {
        degreeOfSeparationChecker.isWithinNConnections(node1 = 101, node2 = 102, numDegrees = 1) must equal(false)
      }

      "return false if input nodes are not part of the same connected graph" in {
        degreeOfSeparationChecker.isWithinNConnections(node1 = 1, node2 = 9, numDegrees = 1) must equal(false)
      }
    }


    "n = 2" must {
      "return true if both input nodes are the same" in {
        degreeOfSeparationChecker.isWithinNConnections(node1 = 1, node2 = 1, numDegrees = 2) must equal(true)
      }

      "return true if both input nodes are directly connected to each other" in {
        degreeOfSeparationChecker.isWithinNConnections(node1 = 1, node2 = 2, numDegrees = 2) must equal(true)

      }

      "return true if input nodes share a common connection" in {
        degreeOfSeparationChecker.isWithinNConnections(node1 = 1, node2 = 3, numDegrees = 2) must equal(true)
        degreeOfSeparationChecker.isWithinNConnections(node1 = 2, node2 = 5, numDegrees = 2) must equal(true)

      }

      "return false if input nodes do not share a common connection" in {
        degreeOfSeparationChecker.isWithinNConnections(node1 = 1, node2 = 4, numDegrees = 2) must equal(false)
      }

      "return false if input nodes are not part of the graph" in {
        degreeOfSeparationChecker.isWithinNConnections(node1 = 101, node2 = 102, numDegrees = 2) must equal(false)
      }

      "return false if input nodes are not part of the same connected graph" in {
        degreeOfSeparationChecker.isWithinNConnections(node1 = 1, node2 = 9, numDegrees = 2) must equal(false)
      }
    }


    "n = 3" must {
      "return true if both input nodes are the same" in {
        degreeOfSeparationChecker.isWithinNConnections(node1 = 1, node2 = 1, numDegrees = 3) must equal(true)
      }

      "return true if both input nodes are directly connected to each other" in {
        degreeOfSeparationChecker.isWithinNConnections(node1 = 1, node2 = 2, numDegrees = 3) must equal(true)

      }

      "return true if input nodes share a common connection" in {
        degreeOfSeparationChecker.isWithinNConnections(node1 = 2, node2 = 5, numDegrees = 3) must equal(true)

      }

      "return true if input nodes are within 3 connections of each other" in {
        degreeOfSeparationChecker.isWithinNConnections(node1 = 1, node2 = 4, numDegrees = 3) must equal(true)
        degreeOfSeparationChecker.isWithinNConnections(node1 = 1, node2 = 5, numDegrees = 3) must equal(true)
      }

      "return false if input nodes are more than 3 connections away from each other" in {
        degreeOfSeparationChecker.isWithinNConnections(node1 = 1, node2 = 7, numDegrees = 3) must equal(false)
      }

      "return false if input nodes are not part of the graph" in {
        degreeOfSeparationChecker.isWithinNConnections(node1 = 101, node2 = 102, numDegrees = 3) must equal(false)
      }

      "return false if input nodes are not part of the same connected graph" in {
        degreeOfSeparationChecker.isWithinNConnections(node1 = 1, node2 = 9, numDegrees = 3) must equal(false)
      }
    }


    "n = 4" must {
      "return true if both input nodes are the same" in {
        degreeOfSeparationChecker.isWithinNConnections(node1 = 1, node2 = 1, numDegrees = 4) must equal(true)
      }

      "return true if both input nodes are directly connected to each other" in {
        degreeOfSeparationChecker.isWithinNConnections(node1 = 1, node2 = 2, numDegrees = 4) must equal(true)

      }

      "return true if input nodes share a common connection" in {
        degreeOfSeparationChecker.isWithinNConnections(node1 = 2, node2 = 5, numDegrees = 4) must equal(true)

      }

      "return true if input nodes are within 3 connections of each other" in {
        degreeOfSeparationChecker.isWithinNConnections(node1 = 1, node2 = 4, numDegrees = 4) must equal(true)
      }

      "return true if input nodes are within 4 connections of each other" in {
        degreeOfSeparationChecker.isWithinNConnections(node1 = 1, node2 = 7, numDegrees = 4) must equal(true)
      }

      "return false if input nodes are more than 4 connections away from each other" in {
        degreeOfSeparationChecker.isWithinNConnections(node1 = 1, node2 = 8, numDegrees = 4) must equal(false)
      }

      "return false if input nodes are not part of the graph" in {
        degreeOfSeparationChecker.isWithinNConnections(node1 = 101, node2 = 102, numDegrees = 4) must equal(false)
      }

      "return false if input nodes are not part of the same connected graph" in {
        degreeOfSeparationChecker.isWithinNConnections(node1 = 1, node2 = 9, numDegrees = 4) must equal(false)
      }
    }
  }
}
