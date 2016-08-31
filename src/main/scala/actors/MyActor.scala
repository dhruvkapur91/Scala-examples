package actors

import akka.actor.{Actor, Props}
import akka.event.Logging

class MyActor extends Actor {

  val log = Logging(context.system,this)

  def receive = {
    case "test" => log.info("Received test")
    case _ => log.info("Received some unknown message")
  }

}

class MyActorWithArgs(message : String) extends Actor {

  val log = Logging(context.system,this)

  def receive = {
    case x : String if x.matches(message) => log.info(s"Received $message")
    case _ => log.info("Received some unknown message")
  }

}

object RunningActor extends App {
  val props1 = Props[MyActor]
  val props2 = Props(new MyActorWithArgs("Hello args")) // This is not recommended
}