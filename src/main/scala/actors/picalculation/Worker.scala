package actors.picalculation

import actors.picalculation.Worker.Receiving.Work
import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props}

class Worker extends Actor with ActorLogging {

  override def receive: Receive = {
    case Work(startIndexOfComputation, numberOfElementsToCompute) =>
      log.info("Starting work")
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
  worker ! Work(1, 1)
  Thread.sleep(1000)
  system.terminate()
}