package scalacheck

import java.io.PrintWriter

import org.scalacheck.Properties

import scala.util.Random

object InventoryData extends Properties("Inventory") {

  /*
  insert into inventory (unit,station_code,frame_id,score) VALUES (1,'10','100',10.0);
  insert into tutorialspoint.inventory (unit,station_code,frame_id,score) VALUES (
   */

  /*

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



  val collection = units.cartesian(frames).sample(true,0.5).map {
    case (unit,frame) => s"$unit, $frame, ${stationCode(frame)}, ${frameScore(frame)}"
  }

   */

  case class Inventory(unit: BigInt, frame_id: Int, station_code: Int, score: Double) {
    def toSql = s"$unit, '$station_code', '$frame_id', $score"
  }

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


  def stationCode() = {
    Random.shuffle(1 to 30).head
  }

  def frameScore() = {
    Random.shuffle(100 to 1000).head
  }

  val numberOfFrames = 40000
  val numberOfUnits = 10000

  val allBooked = for {
    unit <- 1 to numberOfUnits
    frameId <- 1 to numberOfFrames
    if Math.random() > 0.5
  } yield s"$unit, $frameId, ${stationCode()}, ${frameScore()}"

  //  val allQueries: String = allBooked.map(_.toSql).mkString("\n")
  //
  //  new PrintWriter("/Users/dhruvkapur/temp/temp2/filename2") { write(allQueries); close() }
  //
  //  println(allQueries)


}
