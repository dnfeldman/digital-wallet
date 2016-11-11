package digitalwallet.labelers

import digitalwallet.Sink
import digitalwallet.processors.Transaction

/**
  * @param sink where labeled transactions should be persisted
  */
abstract class TransactionLabeler(sink: Sink) {

  final val VALID_LABEL = "trusted"
  final val INVALID_LABEL = "unverified"

  def persist: Unit = sink.persist()

  def label(transaction: Transaction): Unit
}
