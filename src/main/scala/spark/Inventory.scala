package spark

import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD

object Inventory extends App {

  val sc: SparkContext = ???

  import com.datastax.spark.connector._
  import org.apache.spark.sql.cassandra._

  val numberOfFrames = 40000
  val numberOfUnits = 10000

  val units = sc.parallelize(1 to numberOfUnits)
  val frames = sc.parallelize(1 to numberOfFrames)

  def stationCode(frameId: Int) = {
    if (frameId % 2 == 0) {
      1
    } else if (frameId % 3 == 0) {
      2
    } else if (frameId % 5 == 0) {
      3
    } else {
      4
    }
  }

  def frameScore(frameId: Int) = {
    if (frameId % 2 == 0) {
      100
    } else if (frameId % 3 == 0) {
      200
    } else if (frameId % 5 == 0) {
      300
    } else {
      400
    }
  }


  val collection = units.cartesian(frames).sample(withReplacement = true, fraction = 0.5).map {
    case (unit, frame) => (unit, frame.toString, stationCode(frame).toString, frameScore(frame).toDouble)
  }


  collection.saveToCassandra("tutorialspoint","inventory")
  collection.saveToCassandra("tutorialspoint","inventory",SomeColumns("unit","station_code","frame_id","score"))

}
