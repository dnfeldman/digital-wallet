package digitalwallet.processors

import org.joda.time.format.DateTimeFormat
import org.scalatest.{ MustMatchers, WordSpec }

import scala.util.Try

class TransactionLoaderSpec extends WordSpec with MustMatchers {

  val transactionFile = getClass().getResource("./../files/transactions.csv").getPath
  val transactionLoader = new TransactionLoader(transactionFile)

  "TransactionLoader" must {
    "correctly load a file, ignoring headers and return a stream of valid Transactions" in {
      transactionLoader.validTransactions().length must equal(5)
    }
  }

  "TransactionLoader#extractTransaction" must {
    "return successfully parsed Try when the line represents a valid Transaction" in {

      val validTransaction = "2016-01-01 00:01:02, 123, 456, 99.99, text\001\t\rtext"

      val expected = Transaction("2016-01-01 00:01:02", 123, 456, 99.99, "text\001\t\rtext")
      transactionLoader.extractTransaction(validTransaction) must equal(Try(expected))
    }

    "return unsuccessfully parsed Try when the line represents an in valid Transaction" in {
      val inValidTransaction = "2016-01-01 00:01:02, notAnInt, notAnInt, 99.99, text\001\t\rtext"

      transactionLoader.extractTransaction(inValidTransaction).isFailure must equal(true)
    }
  }
}
