package digitalwallet.labelers

import digitalwallet.Sink
import digitalwallet.modules.DegreeOfSeparationChecker
import digitalwallet.processors.Transaction

/**
  * Class that will label a transaction as VALID_LABEL if both people are within 2 degrees
  * of separation from each other
  *
  * @param degreeOfSeparationChecker an 'api client' that will tell this labeler if two nodes are
  *                                  within MAX_DEGREE_FOR_VERIFIED_LABEL degrees of each other
  * @param sink                      where result will be stored
  */
class Feature1(degreeOfSeparationChecker: DegreeOfSeparationChecker, sink: Sink) extends TransactionLabeler(sink) {

  final val MAX_DEGREE_FOR_VERIFIED_LABEL = 1

  /**
    * Obtains a label for given transaction and stores it in the sink
    *
    * @param transaction to be labeled
    * @return
    */
  override def label(transaction: Transaction): Unit = {
    val assignedLabel = assignLabel(transaction)

    sink.put(assignedLabel)
  }

  /**
    * Assigns a VALID_LABEL or INVALID_LABEL to given transaction based on how far both parties are
    * in the social network (which this class interacts with through DegreeOfSeparationChecker)
    *
    * @param transaction to be labeled
    * @return label for the transaction
    */
  private[labelers] def assignLabel(transaction: Transaction): String = {
    if (degreeOfSeparationChecker.isWithinNConnections(transaction.id1, transaction.id2, MAX_DEGREE_FOR_VERIFIED_LABEL)) {
      VALID_LABEL
    } else {
      INVALID_LABEL
    }
  }
}
