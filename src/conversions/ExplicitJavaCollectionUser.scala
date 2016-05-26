package conversions

import java.util

object ExplicitJavaCollectionUser extends App {
  val javaList: util.List[String] = new JavaCollection().createStringList

  println {
    "javalist : " + javaList
    // javaList.map - so such method
  }

  println {
    "pure scala list : " + List(1, 2, 3).map(x => x)
  }

  // So as we see these two lists are definately of different types... lets try to convert this javaList into more of a scala list...

  import collection.JavaConverters._
  import collection.mutable
  import collection.mutable._

  val scalaList: mutable.Buffer[String] = javaList.asScala

  println {
    "converted list : " + scalaList.map(x => x)
  }

  private val scalaCreatedJavaList: util.List[Int] = ArrayBuffer(1, 2, 3).asJava
  val y: mutable.Buffer[Int]  = scalaCreatedJavaList.asScala
  val x: mutable.Seq[Int] = scalaCreatedJavaList.asScala

  println {
    new JavaCollection().returnSame(javaList.asScala.asJava)
  }

  println {
    new JavaCollection().returnSame(List("1","2").asJava)
  }

}
