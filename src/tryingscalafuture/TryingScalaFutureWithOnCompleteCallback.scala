package tryingscalafuture

object TryingScalaFutureWithOnCompleteCallback extends App {

  import scala.concurrent.ExecutionContext.Implicits.global
  import scala.concurrent.Future
  import scala.util.{Random, Success,Failure}

  type CoffeeBeans = String
  type GroundCoffee = String

  case class Water(temperature: Int)

  case class GrindingException(msg:String) extends Exception(msg)

  def grind(beans: CoffeeBeans): Future[GroundCoffee] = Future {
    println(s"started grinding...")
    Thread.sleep(Random.nextInt(2000))
    if(beans == "baked beans")
      throw GrindingException("are you joking?")
    println("finished grinding")
    s"grounded coffee of $beans"
  }

  grind("some awesome beans").onComplete {
    case Success(groundCoffee:GroundCoffee) => println("okay we got the grounded coffee!")
    case Failure(ex) => println("this grinder needs a serious replacement!")
  }

  def heatWater(water: Water): Future[Water] = Future {
    println("Heating water now...")
    Thread.sleep(Random.nextInt(2000))
    println("hot, its hot!")
    water.copy(temperature = 84)
  }

  val fastTemperatureOkay: Future[Boolean] = heatWater(Water(10)).map { water =>
    println("we are in the future")
    (80 to 85).contains(water.temperature)
  }

  def slowTemperatureOkay(water: Water): Future[Boolean] = Future {
    (80 to 85).contains(water.temperature)
  }

  val nestedFuture: Future[Future[Boolean]] = heatWater(Water(10)).map {
    water => slowTemperatureOkay(water)
  }

  val flatFuture: Future[Boolean] = heatWater(Water(10)).flatMap {
    water => slowTemperatureOkay(water)
  }

  val isAcceptable: Future[Boolean] = for {
    h <- heatWater(Water(10))
    ok <- slowTemperatureOkay(h)
  } yield ok

  Thread.sleep(3000)

}
