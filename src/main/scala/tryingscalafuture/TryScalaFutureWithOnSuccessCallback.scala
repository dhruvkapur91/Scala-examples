package tryingscalafuture

object TryScalaFutureWithOnSuccessCallback extends App {

  import scala.concurrent.ExecutionContext.Implicits.global
  import scala.concurrent.Future
  import scala.util.Random

  type CoffeeBeans = String
  type GroundCoffee = String

  case class GrindingException(msg:String) extends Exception(msg)

  def grind(beans: CoffeeBeans): Future[GroundCoffee] = Future {
    println(s"started grinding...")
    Thread.sleep(Random.nextInt(2000))
    if(beans == "baked beans")
      throw GrindingException("are you joking?")
    println("finished grinding")
    s"grounded coffee of $beans"
  }

  grind("some awesome beans").onSuccess {
    case groundedCoffee: GroundCoffee => println("okay we got the grounded coffee!")
  }

}
