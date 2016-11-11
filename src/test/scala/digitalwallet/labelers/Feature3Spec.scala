package digitalwallet.labelers

import digitalwallet.Sink
import digitalwallet.modules.{ DegreeOfSeparationChecker, Graph }
import digitalwallet.processors.Transaction
import org.mockito.Mockito._
import org.scalatest.mock.MockitoSugar
import org.scalatest.{ MustMatchers, WordSpec }

import scala.collection.immutable.IntMap
import scala.collection.mutable


class Feature3Spec extends WordSpec with MustMatchers with MockitoSugar {

  /**
    * 1     3     5      8
    *  \  /  \  / | \  /
    *   2     4   |  7
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
    8 -> mutable.Set(7)
  )
  val graph = new Graph(adjacencyMap)
  val degreeOfSeparationChecker = new DegreeOfSeparationChecker(graph)

  val sink = mock[Sink]
  val transaction = mock[Transaction]


  "Feature3#assignLabel" must {
    val feature3Labeler = new Feature3(degreeOfSeparationChecker, sink)

    "correctly assign VALID_LABEL to transactions that are within 4 connections" in {
      when(transaction.id1).thenReturn(1)
      when(transaction.id2).thenReturn(7)

      val expected = "trusted"
      feature3Labeler.assignLabel(transaction) must equal(expected)
    }

    "correctly assign INVALID_LABEL to any other transactions" in {
      when(transaction.id1).thenReturn(1)
      when(transaction.id2).thenReturn(8)

      val expected = "unverified"
      feature3Labeler.assignLabel(transaction) must equal(expected)
    }
  }
}
