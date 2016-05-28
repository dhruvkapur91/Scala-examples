package exploringeither

import java.lang.Math.random

object ExploringEither extends App {

  case class Person(name: String)

  case object NoPerson

  def getAPerson: Either[Person, NoPerson.type] = {
    if (random() > 0.5) Left(Person("Dhruv")) else Right(NoPerson)
  }

  def printPersonsName(person: Either[Person, NoPerson.type]) = person match {
    case Left(person) => person.name
    case Right(noPerson) => "Name not availabe"
  }

  1 to 10 foreach {
    _ => println {
      printPersonsName(getAPerson)
    }
  }

  private val mappedValue: Either[Person, String] = getAPerson.right.map(x => "hello")

}
