package actors

import akka.actor.Actor
import akka.event.Logging

class MyActor extends Actor {

  val log = Logging(context.system,this)

  def receive = {
    case "test" => log.info("Received test")
    case _ => log.info("Received some unknown message")
  }

}
