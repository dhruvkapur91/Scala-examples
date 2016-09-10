package actors.fsm

import actors.fsm.Buncher.ActorData.{Data, Todo, Uninitialized}
import actors.fsm.Buncher.ActorState.{Active, Idle, State}
import actors.fsm.Buncher.Receiving.{Flush, Queue, SetTarget}
import actors.fsm.Buncher.Sending.Batch
import akka.actor.{Actor, ActorLogging, ActorRef, FSM, Props}

import scala.concurrent.duration._

class Buncher extends FSM[State,Data] {
  startWith(Idle,Uninitialized)

  when(Idle) {
    case Event(SetTarget(ref),Uninitialized) =>
      stay using Todo(ref,Vector.empty)
  }

  when(Active, stateTimeout = 1 second) {
    case Event(Flush | StateTimeout, todo : Todo) =>
      goto(Idle) using todo.copy(queue = Vector.empty)
  }

  whenUnhandled {
    case Event(Queue(newObject), oldTodo @ Todo(_,oldObjects)) =>
      goto(Active) using oldTodo.copy(queue = oldObjects :+ newObject)

    case Event(someEvent, someState) =>
      log.warning(s"Unhandled request $someEvent in state $stateName / $someState")
      stay
  }

  onTransition {
    case Active -> Idle =>
      stateData match {
        case Todo(anotherActor : ActorRef, queue) => anotherActor ! Batch(queue)
        case _ => // do nothing
      }
  }

  initialize()
}

object Buncher {

  def createBuncher = Props(classOf[Buncher])

  object Receiving {
    final case class SetTarget(ref : ActorRef)
    final case class Queue(obj:Object)
    case object Flush
  }

  object Sending {
    final case class Batch(obj : Seq[Any])
  }

  object ActorState {
    sealed trait State
    case object Idle extends State
    case object Active extends State
  }

  object ActorData {
    sealed trait Data
    case object Uninitialized extends Data
    final case class Todo(target : ActorRef, queue : Seq[Any]) extends Data
  }
}

class Customer extends Actor with ActorLogging {
  override def receive: Receive = {
    case Batch(objects) => objects.foreach(obj => log.info(s"$obj"))
  }
}

object Customer {
  def createCustomer = Props(classOf[Customer])
}

