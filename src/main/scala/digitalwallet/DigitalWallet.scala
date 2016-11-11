package digitalwallet

import digitalwallet.labelers.{ Feature1, Feature2, Feature3 }
import digitalwallet.modules.{ DegreeOfSeparationChecker, Graph }
import digitalwallet.processors.TransactionLoader

import scala.collection.immutable.IntMap
import scala.collection.mutable.Map

/**
  * Main application runner. This will generate preliminary social graph from batchPaymentSource,
  * then, for each new transaction from streamPaymentSource, it will generate 3 labels based on the
  * degree of connectedness between people in that transaction.
  * After the transaction has been labeled by all 3 of the labelers, it will be added to the social
  * graph so that the next transaction will be processed using most up-to-date graph
  */
object DigitalWallet extends App {
  val params = Params.parseArgs(args)

  // Prepare to stream transactions from batchPaymentSource
  val batchPayments = new TransactionLoader(params("batchPaymentSource"))

  // Build initial network (from batchPaymentSource
  val graph = new Graph(IntMap[scala.collection.mutable.Set[Int]]())
  batchPayments.validTransactions().foreach(transaction =>
    graph.addConnection(transaction.id1, transaction.id2)
  )

  // Prepare transaction labelers with the 'api' to query our graph and a sink for where to store labeled transactions
  val degreeOfSeparationChecker = new DegreeOfSeparationChecker(graph)
  val labelers = Set(
    new Feature1(degreeOfSeparationChecker, Sink(params("feature1Output"))),
    new Feature2(degreeOfSeparationChecker, Sink(params("feature2Output"))),
    new Feature3(degreeOfSeparationChecker, Sink(params("feature3Output")))
  )


  // Prepare to stream transactions from streamPaymentSource
  val streamPayments = new TransactionLoader(params("streamPaymentSource"))

  // For each transaction, let each transaction labeler label it, then add it our graph
  streamPayments.validTransactions().foreach(transaction => {
    labelers.foreach(labeler => labeler.label(transaction))

    graph.addConnection(transaction.id1, transaction.id2)
  }
  )

  // Save generated labels
  labelers.foreach(labeler => labeler.persist)
}


object Params {
  def parseArgs(args: Array[String]): Map[String, String] = {
    args.map(arg => addToParams(arg))

    params
  }

  private val params: Map[String, String] = Map(
    "batchPaymentSource" -> "./paymo_input/batch_payment.txt",
    "streamPaymentSource" -> "./paymo_input/stream_payment.txt",
    "feature1Output" -> "./paymo_output/output2.txt",
    "feature2Output" -> "./paymo_output/output2.txt",
    "feature3Output" -> "./paymo_output/output3.txt"
  )

  private def addToParams(argString: String): Unit = {
    val Array(paramName, paramValue) = argString.split("=")

    if (params.isDefinedAt(paramName.trim)) {
      params.update(paramName, paramValue.trim)
    } else {}
  }
}