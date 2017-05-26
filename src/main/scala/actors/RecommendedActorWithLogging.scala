package actors

import actors.RecommendedActorWithActorLogging.createRecommendedActor
import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props}

class RecommendedActorWithActorLogging(greeting : String) extends Actor with ActorLogging {

  override def receive: Receive = {
    case msg : String if msg.matches(greeting) => log.info(greeting)
    case _  => log.info("Unexpected message found!!")
  }
}

object RecommendedActorWithActorLogging {
  def createRecommendedActor(greeting : String) = Props(classOf[RecommendedActorWithActorLogging],greeting)
}

object RecommendedActorWithActorLoggingRunner extends App {
  val actorSystem = ActorSystem.create("NewActorSystem")
  private val recommendedActor: ActorRef = actorSystem.actorOf(createRecommendedActor("YAYY!"))
  recommendedActor ! "YAYY!"
  recommendedActor ! "No YAYY!"
//  actorSystem.terminate()
}
