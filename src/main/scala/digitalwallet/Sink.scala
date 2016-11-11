package digitalwallet

import java.io.{ File, PrintWriter }

/**
  * An interface for writing to files
  *
  * @param saveTo path to file
  */
case class Sink(saveTo: String) {
  val file = new PrintWriter(new File(saveTo))
  var isEmpty = true

  /**
    *
    * @param entry to write to file
    * @return
    */
  def put(entry: String): Unit = {
    if (isEmpty) {
      file.write(entry)
      isEmpty = false

    } else {
      file.write("\n" + entry)
    }
  }

  def persist(): Unit = {
    file.close()
  }
}