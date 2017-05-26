package spark

import org.apache.spark.sql.SparkSession
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.immutable.IndexedSeq
import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global


object SequenialSaves extends App {

  val sparkConf = new SparkConf().setMaster("local[*]").setAppName("Trying parallel stuff")
  val sc = new SparkContext(sparkConf)

  sc.parallelize(1 to 100000).saveAsTextFile("file1")
  sc.parallelize(1 to 100000).saveAsTextFile("file2")
  sc.parallelize(1 to 100000).saveAsTextFile("file3")

  while (true) {}

}


object SequenialSavesWithLessCores extends App {
  val sparkConf = new SparkConf().setMaster("local[*]").setAppName("Trying parallel stuff")
  val sc = new SparkContext(sparkConf)

  sc.parallelize(1 to 10000000,2).saveAsTextFile("file1")
  sc.parallelize(1 to 10000000,2).saveAsTextFile("file2")
  sc.parallelize(1 to 10000000,2).saveAsTextFile("file3")

  while (true) {}

}

object ParallelSavesWithLessCores extends App {
  val sparkConf = new SparkConf().setMaster("local[*]").setAppName("Trying parallel stuff")
  val sc = new SparkContext(sparkConf)

  val saveFileOne = Future { sc.parallelize(1 to 10000000,2).saveAsTextFile("file1")}
  val saveFileTwo = Future { sc.parallelize(1 to 10000000,2).saveAsTextFile("file2")}
  val saveFileThree = Future { sc.parallelize(1 to 10000000,2).saveAsTextFile("file3") }

  val saveAllFiles = for {
    fileOne <- saveFileOne
    fileTwo <- saveFileTwo
    fileThree <- saveFileThree
  } yield (fileOne,fileTwo,fileThree)

  saveAllFiles onSuccess {
    case result =>
      """
        |Saved all the files!
      """.stripMargin
  }


  while (true) {}


}



object ParallelSaves extends App {
  val sparkConf = new SparkConf().setMaster("local[*]").setAppName("Trying parallel stuff")
  val sc = new SparkContext(sparkConf)

  val saveFileOne = Future { sc.parallelize(1 to 10000000).saveAsTextFile("file1")}
  val saveFileTwo = Future { sc.parallelize(1 to 10000000).saveAsTextFile("file2")}
  val saveFileThree = Future { sc.parallelize(1 to 10000000).saveAsTextFile("file3") }

  val saveAllFiles = for {
    fileOne <- saveFileOne
    fileTwo <- saveFileTwo
    fileThree <- saveFileThree
  } yield (fileOne,fileTwo,fileThree)

  saveAllFiles onSuccess {
    case result =>
      """
        |Saved all the files!
      """.stripMargin
  }


  while (true) {}


}
