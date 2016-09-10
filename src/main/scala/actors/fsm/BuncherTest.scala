package actors.fsm

import actors.fsm.Buncher.ActorData.{Todo, Uninitialized}
import actors.fsm.Buncher.ActorState.{Active, Idle}
import actors.fsm.Buncher.Receiving.{Flush, Queue, SetTarget}
import actors.fsm.Buncher.Sending.Batch
import akka.actor.{Actor, ActorLogging, ActorSystem, Props}
import akka.testkit.TestFSMRef
import org.scalatest._

class SomeActor extends Actor with ActorLogging {
  def receive = {
    case Batch(somethings) => log.info(s"Recieved $somethings")
  }
}

class BuncherTest extends FunSpec with ShouldMatchers {

  implicit val testActorSystem: ActorSystem = ActorSystem("TestActorSystem")


  describe("The buncher") {
    it("should start in Idle state with Unitialized data") {
      val testBuncher = TestFSMRef(new Buncher)
      testBuncher.stateName should be(Idle)
      testBuncher.stateData should be(Uninitialized)
    }

    it("should stay in Idle state when registered with another actor like SomeActor") {
      val someActor = testActorSystem.actorOf(Props[SomeActor])
      val testBuncher = TestFSMRef(new Buncher)
      testBuncher ! SetTarget(someActor)

      testBuncher.stateName should be(Idle)
      testBuncher.stateData should be(Todo(someActor,Vector.empty))
    }

    it("should goto Active state when registered with queued with a message") {
      val someActor = testActorSystem.actorOf(Props[SomeActor])
      val testBuncher = TestFSMRef(new Buncher)
      testBuncher ! SetTarget(someActor)
      testBuncher ! Queue("Hello")
      testBuncher.stateName should be(Active)
      testBuncher.stateData should be(Todo(someActor,Seq("Hello")))
    }

    it("should flush messages when flush is called") {
      val someActor = testActorSystem.actorOf(Props[SomeActor])
      val testBuncher = TestFSMRef(new Buncher)
      testBuncher ! SetTarget(someActor)
      testBuncher ! Queue("Hello")
      testBuncher ! Flush
      testBuncher.stateName should be(Idle)
      testBuncher.stateData should be(Todo(someActor,Vector.empty))
    }

  }
}