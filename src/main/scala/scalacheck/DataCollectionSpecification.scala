package scalacheck

import org.scalacheck.Prop._
import org.scalacheck.{Arbitrary, Gen, Properties}
import org.scalatest.PropSpec
import org.scalatest.prop.Checkers

case class Rectangle(width:Double, height:Double) {
  // when the width is a multiple of 3, this will fail
  lazy val area =  if(width % 11 ==0) width * 1.0001 * height else width * height
  // valid version of the method above
  lazy val areaCorrect: Double = width * height
  lazy val perimeter = (2*width) + (2*height)
  def biggerThan(r:Rectangle) = area > r.area
}

object SimpleGenerator extends Properties("Simple sample generator") {
  // simple generator based on the example given in the ScalaCheck documentation
  // TODO: create a more suitable one?
  val myGen: Gen[(Int, Int)] = for {
    n <- Gen.choose(10,20)
    m <- Gen.choose(2*n, 500)
  } yield (n,m)

  // simple function
  def myFunction(n1:Int, n2:Int) = n1 + n2

  property("Test property with generator") = forAll(myGen) {(n:(Int,Int)) =>
    n match {
      case (n1, n2) => (n1 + n2) == myFunction(n1,n2)
    }
  }
}

class ArbitraryRectangleWithCheckersSpec extends PropSpec with Checkers {
  import RectangleGenerator._


  property("A rectangle should correctly calculate its area") {
    check(forAll { (r:Rectangle) =>
      r.area == (r.width * r.height)
    })
  }
  property("A rectangle should be able to identify which rectangle is bigger") {
    check(forAll { (r1:Rectangle, r2:Rectangle) =>
      (r1 biggerThan r2) == (r1.area > r2.area)
    })
  }
}

/**
  * Specification with a Generator that is used to create case classes and verify the
  * data in them.
  *
  * In this example the generator returns both the case class with specific values as well as
  * the specific values that were used to genreate the case class, so that its calculations
  * can be verified
  */
object RectangleSpecification extends Properties("Rectangle specification") {

  val rectangleGen:Gen[(Rectangle, Double,Double)] = for {
    height <- Gen.choose(0d,9999d)
    width <- Gen.choose(0d,9999d)
  } yield (Rectangle(width, height), width, height)

  property("Test area") = forAll(rectangleGen) { (input:(Rectangle,Double,Double)) => input match {
    case(r, width, height) => r.area == width * height
  }}
}

/**
  * Generator of case objects for the Rectangle class, as well as an arbitrary generator
  */
object RectangleGenerator {
  // generator for the Rectangle case class
  val rectangleGen:Gen[Rectangle] = for {
    height <- Gen.choose(0,9999)
    width <- Gen.choose(0,9999)
  } yield Rectangle(width, height)

  // Arbitrary generator of rectangles
  implicit val arbRectangle: Arbitrary[Rectangle] = Arbitrary(rectangleGen)
}

/**
  * This property shows the advantage of using an arbitrary generator, as ScalaCheck will then
  * be able to automatically generate the test data using the implicit arbitrary generator in scope,
  * and we don't need to provide a generator object as a parameter to the forAll method
  *
  * In this case, the arbitrary generator is in the scope after the import statement to import
  * the arbitrary function
  */
object ArbitraryRectangleSpecification extends Properties("Rectangle specification with an Arbitrary generator") {
  import RectangleGenerator._

  // generate two random rectangles and check which one is bigger
  property("Test biggerThan") = forAll{ (r1:Rectangle, r2:Rectangle) =>
    (r1 biggerThan r2) == (r1.area > r2.area)
  }

  // please note that if not using an arbitrary generator, then we need to provide a generator parameter
  // for each one of the random data parameters. Therefore, the following code is equivalent to the
  // one above:
  property("Test biggerThan") = forAll(rectangleGen, rectangleGen){ (r1:Rectangle, r2:Rectangle) =>
    (r1 biggerThan r2) == (r1.area > r2.area)
  }
}

object DataCollectionSpecification extends Properties("Data collection examples") {
  import RectangleGenerator._

  /**
    * The Prop.collect method is the easiest way to group our test data, but depending on the classification
    * criteria for our data, it is possible that data cannot be grouped in a meaningful way.
    * In the following example, the console output will show a long list of pairs of
    * height and width, each one of them with a 1% frequency
    */
  property("data collection spec with Prop.collect") = forAll { (r:Rectangle) =>
    collect((r.width, r.height)) {
      r.areaCorrect == (r.width * r.height)
    }
  }

  /**
    * Helper function to help us classify input data. Using a partfial function we can plug in some pattern
    * matching with guards that define how our input data gets classified. The return value of the function
    * is a string (this function always returns something) that is used by ScalaCheck as the grouping criteria
    * for our data
    */
  val collector: PartialFunction[Rectangle,String] = {
    case r if r.perimeter < 10000 => "small"
    case r if r.perimeter > 10000 && r.perimeter < 25000 => "medium"
    case r if r.perimeter > 25000 => "large"
  }

  /**
    * This is the same check as the previous example, but now data is grouped using our collector
    * custom function. The console output now is suddenly more meaningful for analysis
    */
  property("data collection spec with Prop.collect and a grouping function") = forAll { (r:Rectangle) =>
    collect(collector(r)) {
      r.areaCorrect == (r.width * r.height)
    }
  }

  /**
    * Here we use the "binary" version of Prop.classify to classify our random rectangle objects into "taller" or "wider",
    * and then with Prop.collect we've collected the data using our previous method. The output now is a two-level
    * grouping of our data.
    * The example also shows how Prop.classify and Prop.collect can be combined within the same property check,
    * and even multiple calls can be nested to obtain a more granular classification
    */
  property("data collection spec with Prop.classify and Prop.collect") = forAll { (r:Rectangle) =>
    classify(r.height > r.width, "taller", "wider") {
      collect(collector(r)) {
        r.areaCorrect == (r.width * r.height)
      }
    }
  }
}