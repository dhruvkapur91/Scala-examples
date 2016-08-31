package actors

import actors.RecommendedActorWithEnumeratedClasses.{GoodBye, Greeting, createRecommendedActor}
import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props}

class RecommendedActorWithEnumeratedClasses(greeting : String) extends Actor with ActorLogging {

  override def receive: Receive = {
    case Greeting(msg) if msg.matches(greeting) => log.info(greeting)
    case GoodBye => log.info("So long and thanks for all the fish!")
    case _  => log.info("Unexpected message found!!")
  }
}

object RecommendedActorWithEnumeratedClasses {
  def createRecommendedActor(greeting : String) = Props(classOf[RecommendedActorWithEnumeratedClasses],greeting)

  case class Greeting(msg : String)
  case object GoodBye
}

object RecommendedActorWithEnumeratedClassesRunner extends App {
  val actorSystem = ActorSystem("NewActorSystem")
  private val recommendedActor: ActorRef = actorSystem.actorOf(createRecommendedActor("YAYY!"))
  recommendedActor ! Greeting("YAYY!")
  recommendedActor ! "No YAYY!"
  recommendedActor ! GoodBye
  actorSystem.terminate()
}
