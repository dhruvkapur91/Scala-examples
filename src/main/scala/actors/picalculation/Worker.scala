package actors.picalculation

import actors.picalculation.Worker.Receiving.Work
import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props}

import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.Await
import scala.concurrent.duration._


class Worker extends Actor with ActorLogging {

  override def receive: Receive = {
    case Work(startIndexOfComputation, numberOfElementsToCompute) =>
      sender ! "Starting the work"
  }
}

object Worker {

  sealed trait ValidMessages

  object Receiving {

    case class Work(startIndexOfComputation: Int, numberOfElementsToCompute: Int) extends ValidMessages

  }

}

object PiCalculatorRunner extends App {
  val system: ActorSystem = ActorSystem("PiCalculatorActor")
  private val worker: ActorRef = system.actorOf(Props[Worker])

  implicit val timeout = Timeout(5 seconds)
  private val future = worker ? Work(1,1)
  println(Await.result(future, timeout.duration).asInstanceOf[String])

  system.terminate()
}