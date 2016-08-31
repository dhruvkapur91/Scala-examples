package actors

import actors.RecommendedActor.createRecommendedActor
import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.event.Logging

class RecommendedActor(greeting : String) extends Actor {

  val logging = Logging(context.system,this)

  override def receive: Receive = {
    case msg : String if msg.matches(greeting) => logging.info(greeting)
    case _  => logging.info("Unexpected message found!!")
  }
}

object RecommendedActor {
  def createRecommendedActor(greeting : String) = Props(classOf[RecommendedActor],greeting)
}

object Runner extends App {
  val actorSystem = ActorSystem.create("NewActorSystem")
  private val recommendedActor: ActorRef = actorSystem.actorOf(createRecommendedActor("YAYY!"))
  recommendedActor ! "YAYY!"
  recommendedActor ! "No YAYY!"
  actorSystem.terminate()
}
