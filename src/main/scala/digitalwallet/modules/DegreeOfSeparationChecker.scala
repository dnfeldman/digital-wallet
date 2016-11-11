package digitalwallet.modules

/**
  * An 'api' to query a graph to determine if two nodes are within N connections of each other
  * @param graph which contains relationships between nodes to be tested
  */
class DegreeOfSeparationChecker(graph: Graph) {

  /**
    * Checks if two nodes are within numDegrees (read: degree of separation) of each other
    * @param node1 node
    * @param node2 node
    * @param numDegrees of separation
    * @return true/false
    */
  def isWithinNConnections(node1: Int, node2: Int, numDegrees: Int): Boolean = {

    val socialCircle1 = Set[Int](node1)
    val socialCircle2 = Set[Int](node2)

    val startingDegree = 0

    verifyConnectednessUptoNumDegrees(socialCircle1, socialCircle2, startingDegree, numDegrees)
  }

  /**
    * Checks (recursively) if there is an overlap between two social circles up to maxDegreeDepth
    * by expanding currentDegree.
    * @param socialCircle1 nodes that are all within (maxDegreeDepth - currentDegree) of connections
    *                      from the center (node1)
    * @param socialCircle2 nodes that are all within (maxDegreeDepth - currentDegree) of connections
    *                      from the center (node2)
    * @param currentDegree current combined degree of both socialCircles
    * @param maxDegreeDepth maximum combined degree of both socialCircles that we willing to check
    * @return true/false
    */
  private[modules] def verifyConnectednessUptoNumDegrees(socialCircle1: Set[Int], socialCircle2: Set[Int], currentDegree: Int, maxDegreeDepth: Int): Boolean = {

    if (isOverlapPresent(socialCircle1, socialCircle2)) {
      return true
    } else if (currentDegree < maxDegreeDepth) {
      if (currentDegree % 2 == 0) {
        verifyConnectednessUptoNumDegrees(expandByOneDegree(socialCircle1), socialCircle2, currentDegree + 1, maxDegreeDepth)
      } else {
        verifyConnectednessUptoNumDegrees(expandByOneDegree(socialCircle2), socialCircle1, currentDegree + 1, maxDegreeDepth)
      }
    } else {
      return false
    }
  }


  /**
    * @param socialCircle set of nodes representing our current social circle
    * @return set of all the neighbors of nodes from the socialCircle
    */
  private[modules] def expandByOneDegree(socialCircle: Set[Int]): Set[Int] = {
    socialCircle.foldLeft(Set[Int]()) { case(nextDegreeConnections, currentNode) => nextDegreeConnections ++ graph.neighborsOf(currentNode) }
  }


  private[modules] def isOverlapPresent(socialCircle1: Set[Int], socialCircle2: Set[Int]): Boolean = {
    if(socialCircle1.size >= socialCircle2.size) {
      socialCircle2.intersect(socialCircle1).nonEmpty
    } else {
      socialCircle1.intersect(socialCircle2).nonEmpty
    }
  }
}
