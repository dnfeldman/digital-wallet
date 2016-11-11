package digitalwallet.modules

import scala.collection.immutable.{ IntMap, LongMap }
import scala.collection.mutable.Map
import scala.collection.mutable.Set
/**
  * Created by hriundel on 11/5/16.
  */
class Graph(var adjacencyMap: IntMap[Set[Int]]) {

  /**
    *
    * @param nodeId for which to return neighbors
    * @return set of nodes connected to nodeId
    */
  def neighborsOf(nodeId: Int): Set[Int] = adjacencyMap.get(nodeId) match {
    case Some(neighbors) => neighbors
    case None => Set[Int]()
  }

  def addConnection(node1: Int, node2: Int): Unit = {
    val adjacencySetNode1 = adjacencyMap.getOrElse(node1, Set[Int]()) + node2
    val adjacencySetNode2 = adjacencyMap.getOrElse(node2, Set[Int]()) + node1

    //adjacencyMap(node1) = adjacencySetNode1
    //adjacencyMap(node2) = adjacencySetNode2

    adjacencyMap = adjacencyMap ++ (IntMap[Set[Int]](node1 -> adjacencySetNode1) ++ IntMap[Set[Int]](node2 -> adjacencySetNode2))
  }
}