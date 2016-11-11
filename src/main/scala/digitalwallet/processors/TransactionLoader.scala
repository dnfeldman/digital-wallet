package digitalwallet.processors

import scala.collection.immutable.Stream.Empty
import scala.util.Try

/**
  * Created by hriundel on 11/7/16.
  */
class TransactionLoader(filename: String, delimiter: String = ",") {

  private final val TIME_POS = 0
  private final val ID1_POS = 1
  private final val ID2_POS = 2
  private final val AMOUNT_POS = 3
  private final val MESSAGE_POS = 4

  def transactions = scala.io.Source.fromFile(filename).getLines().drop(1)

  def validTransactions(stream: Stream[String] = transactions.toStream): Stream[Transaction] = {
    if(stream.isEmpty) Empty
    else if (extractTransaction(stream.head).isSuccess) extractTransaction(stream.head).get #:: validTransactions(stream.tail)
    else validTransactions(stream.tail)
  }

  def extractTransaction(line: String): Try[Transaction] = Try(extractionAttempt(line))

  private[processors] def extractionAttempt(line: String): Transaction = {
    val fields = line.split(delimiter).map(_.trim)

    // We are not using time for anything (yet), so we can just store it as a string for now
    val time = fields(TIME_POS)

    val id1 = fields(ID1_POS).toInt
    val id2 = fields(ID2_POS).toInt
    val amount = fields(AMOUNT_POS).toDouble
    val message = fields(MESSAGE_POS)
    Transaction(time, id1, id2, amount, message)
  }
}



case class Transaction(time: String, id1: Int, id2: Int, amount: Double, message: String) {
  override def equals(that: Any) = that match {
    case otherTransaction: Transaction =>
      otherTransaction.time.equals(time) &&
      otherTransaction.id1.equals(id1) &&
      otherTransaction.id2.equals(id2) &&
      otherTransaction.amount.equals(amount) &&
      otherTransaction.message.equals(message)

    case _ => false
  }
}