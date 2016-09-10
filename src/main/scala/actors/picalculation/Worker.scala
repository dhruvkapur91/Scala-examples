package actors.picalculation

import actors.picalculation.Master.Receiving.Calculate
import actors.picalculation.Worker.Receiving.Work
import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props}


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

class Master extends Actor with ActorLogging {

  val worker = context.actorOf(Props[Worker],"Worker")

  override def receive: Receive = {
    case Calculate => worker ! Work(1,1)
    case x : Any => println(s"Received some message $x from sender $sender")
  }
}

object Master {
  sealed trait ValidMessages

  object Receiving {
    case object Calculate extends ValidMessages
  }
}

object PiCalculatorRunner extends App {
  val system: ActorSystem = ActorSystem("PiCalculatorActor")
  private val master: ActorRef = system.actorOf(Props[Master],"Master")

  master ! Calculate

  Thread.sleep(1000)
  system.terminate()
}