package solitare

import solitare.Card._
import solitare.Game._
import solitare.Lane._
import solitare.Lanes._

object Card {


  trait Card {
    def canBeMovedAlongWith(last: Card): Boolean

    def toPack = Seq(this)
  }

  type Number = Int

  case class NormalCard(number: Number, color: String) extends Card {
    def isPredessorOf(otherCard: NormalCard) = (otherCard.number - number == 1) && (color == otherCard.color)

    override def canBeMovedAlongWith(last: Card): Boolean = last match {
      case NormalCard(previousNumber, previousColor) => (number - previousNumber == 1) && (color != previousColor)
      case _ => false
    }
  }

  case class DragonCard(name: String) extends Card {
    override def canBeMovedAlongWith(last: Card): Boolean = false
  }

  case object Rose extends Card {
    override def canBeMovedAlongWith(last: Card): Boolean = false
  }

  val reds = (1 to 9).map(cardNumber => cardNumber -> NormalCard(cardNumber, "Red")).toMap
  val greens = (1 to 9).map(cardNumber => cardNumber -> NormalCard(cardNumber, "Green")).toMap
  val blacks = (1 to 9).map(cardNumber => cardNumber -> NormalCard(cardNumber, "Black")).toMap
  val redDragons = (1 to 4).map(index => index -> DragonCard("Red")).toMap
  val greenDragons = (1 to 4).map(index => index -> DragonCard("Green")).toMap
  val blackDragons = (1 to 4).map(index => index -> DragonCard("Black")).toMap

  def getDuplicateCards(cards: Seq[Card]) = {
    cards.groupBy(_.hashCode()).filter {
      case (_, cards: Seq[Card]) => cards.length != 1
    }.values.map(_.head)
  }


}

object Lane {

  type Pack = Seq[Card]

  val nothingToMove = Seq.empty[Pack]

  trait Lane

  case class NormalLane(pack: Pack) extends Lane {
    val normalCards = pack.filter {
      case normalCard: NormalCard => true
      case _ => false
    }

    assert(getDuplicateCards(normalCards).isEmpty,
      s"""
         |A lane should not contain any duplicate card, but did.
         |Original cards in the lane are : $pack
         |And the duplicated cards are : ${getDuplicateCards(normalCards)}
      """.stripMargin)

    private def largestMovablePack(packSoFar: Pack, remainingPack: Pack): Pack = {
      (packSoFar, remainingPack) match {
        case (Nil, Nil) => Seq()
        case (Nil, someCard :: remainingCards) => largestMovablePack(Seq(someCard), remainingCards)
        case (allCardsAreMovable, Nil) => allCardsAreMovable
        case (someMovableCards, maybeMoreMovableCards) if someMovableCards.last.canBeMovedAlongWith(maybeMoreMovableCards.head) => largestMovablePack(someMovableCards :+ maybeMoreMovableCards.head, maybeMoreMovableCards.tail)
        case (allMovableCards, noMoreMovableCards) => allMovableCards
      }
    }

    private def getLargestMovablePack = largestMovablePack(Seq(), pack)

    def getAllMovablePacks = {
      val largestMovablePack = getLargestMovablePack
      (1 to largestMovablePack.length).map(sizeOfThisPack => largestMovablePack.take(sizeOfThisPack))
    }

    def getMovableCard = {
      pack.headOption
    }

    def canAcceptPack(otherPack: Pack) = (pack, otherPack) match {
      case (Nil, Nil) => false
      case (Nil, _) => true
      case (_, Nil) => false
      case _ => pack.head.canBeMovedAlongWith(otherPack.last)
    }

    def removeSubsetPack(subsetPack: Pack) = NormalLane(pack.filterNot(card => subsetPack.contains(card)))

    def addPack(otherPack: Pack) = NormalLane(otherPack ++ pack)
  }

  case class ReserveLane(slot: Option[Card]) extends Lane {
    def canAcceptCard(otherCard: Card) = slot.isEmpty
  }

  case class DrawLane(slot: Option[NormalCard]) extends Lane {
    def canAcceptCard(otherCard: NormalCard): Option[DrawLane] = slot match {
      case None if otherCard.number == 1 => Some(DrawLane(Some(otherCard)))
      case Some(card) if card.isPredessorOf(otherCard) => Some(DrawLane(Some(otherCard)))
      case _ => None
    }
  }

}

object Lanes {

  case class NormalLanes(normalLanes: Seq[NormalLane]) {


    val allNormalCards = normalLanes.flatMap(normalLane => normalLane.normalCards)

    assert(getDuplicateCards(allNormalCards).isEmpty,
      s"""
         |None of the normal cards are supposed to be duplicated in across the lanes (or within, for that matter).
         |And the lane with duplicated cards are : ${getDuplicateCards(allNormalCards).flatMap(duplicatedCard => normalLanes.filter(_.pack.contains(duplicatedCard))).mkString("\n")}
      """.stripMargin)
  }

  case class ReserveLanes(reserveLanes: Seq[ReserveLane]) {
    def getMovableCards: Seq[Card] = reserveLanes.flatMap(_.slot)

    def canAcceptCard(card: Card): Option[ReserveLane] = {
      val freeLanes = for {
        aReserveLane <- reserveLanes
        if aReserveLane.slot.isEmpty
      } yield aReserveLane
      freeLanes.headOption
    }
  }

  case class DrawLanes(drawLanes: Seq[DrawLane]) {

    assert(drawLanes.length == 3, "There should be 3 slots in the draw lane")

    def canAcceptCard(card: Card) = card match {
      case redCard@NormalCard(_, "Red") => drawLanes.head.canAcceptCard(redCard)
      case greenCard@NormalCard(_, "Green") => drawLanes(1).canAcceptCard(greenCard)
      case blackCard@NormalCard(_, "Black") => drawLanes.last.canAcceptCard(blackCard)
      case _ => None
    }
  }

}

class Player(game: Game) {
}

object Game {

  case class AcceptableMove(source: Lane, pack: Pack, destination: Lane) {
    override def toString =
      s"""
         | the pack => $pack can be moved from the
         | source: $source lane to
         | destination: $destination lane
       """.stripMargin
  }

  case class Game(normalLanes: NormalLanes, reserveLanes: ReserveLanes, drawLanes: DrawLanes) {
    def allMovablePacks: Seq[Pack] = {
      val packsMovableFromNormalLanes = normalLanes.normalLanes.flatMap(_.getAllMovablePacks)
      val cardsMovableFromReserveLanes = reserveLanes.getMovableCards
      (packsMovableFromNormalLanes ++ Seq(cardsMovableFromReserveLanes)).filter(_.nonEmpty)
    }

    def legalMoves = {
      val normalToNormalAcceptableMoves = for {
        sourceLane: NormalLane <- normalLanes.normalLanes
        normalLanePack: Pack <- sourceLane.getAllMovablePacks
        destinationLane: NormalLane <- normalLanes.normalLanes
        if destinationLane.canAcceptPack(normalLanePack)
      } yield AcceptableMove(sourceLane, normalLanePack, destinationLane)

      val normalToReserveAcceptableMoves = for {
        sourceLane: NormalLane <- normalLanes.normalLanes
        normalLaneCard <- sourceLane.getMovableCard
        reserveLane <- reserveLanes.canAcceptCard(normalLaneCard)
      } yield AcceptableMove(sourceLane, Seq(normalLaneCard), reserveLane)

      val normalToDrawAcceptableMoves = for {
        sourceLane: NormalLane <- normalLanes.normalLanes
        normalLaneCard <- sourceLane.getMovableCard
        drawLane <- drawLanes.canAcceptCard(normalLaneCard)
      } yield AcceptableMove(sourceLane, Seq(normalLaneCard), drawLane)

//      val reserveLaneToDrawLaneMoves = for {
//        movableCard <- reserveLanes.getMovableCards
//        drawLane <- drawLanes.drawLanes
//        if(drawLane.canAcceptCard(movableCard))
//      }

      normalToNormalAcceptableMoves ++ normalToReserveAcceptableMoves ++ normalToDrawAcceptableMoves
    }

    def playAMove(acceptableMove: AcceptableMove): Game = {
      assert(legalMoves.contains(acceptableMove), s"The move $acceptableMove is not a legal move!")
      acceptableMove match {
        case AcceptableMove(NormalLane(originalSourcePack), packToShuffle, NormalLane(originalDestinationPack)) => {
          val sourceLane = NormalLane(originalSourcePack)
          val destinationLane = NormalLane(originalDestinationPack)
          val newSourceLane = sourceLane.removeSubsetPack(packToShuffle)
          val newDestinationLane = destinationLane.addPack(packToShuffle)
          val restOfTheLanes = normalLanes.normalLanes.filterNot(lane => lane.equals(sourceLane) || lane.equals(destinationLane))
          Game(NormalLanes(restOfTheLanes ++ Seq(newSourceLane, newDestinationLane)), reserveLanes, drawLanes)
        }
      }
    }

    override def toString =
      s"""
         |The game comprises of:
         |Normal lanes ==>
         |${normalLanes.normalLanes.mkString("\n")}
         |
        |Reserve lanes ==>
         |${reserveLanes.reserveLanes.mkString("\n")}
         |
        |Draw lanes ==>
         |${drawLanes.drawLanes.mkString("\n")}
      """.stripMargin

  }


  val game = Game(
    normalLanes = NormalLanes(normalLanes = Seq(
      NormalLane(pack = Seq(blacks(5), reds(4), greenDragons(1), greens(1), reds(9)).reverse),
      NormalLane(pack = Seq(blacks(8), greenDragons(2), blackDragons(1), blacks(1), blackDragons(2)).reverse),
      NormalLane(pack = Seq(reds(2), blacks(4), greenDragons(3), blacks(6), blackDragons(3)).reverse),
      NormalLane(pack = Seq(greens(2), reds(6), reds(3), redDragons(1), blacks(9)).reverse),
      NormalLane(pack = Seq(redDragons(2), greens(3), redDragons(3), reds(5), greens(9)).reverse),
      NormalLane(pack = Seq(greens(5), greens(4), greenDragons(4), blacks(7), redDragons(4)).reverse),
      NormalLane(pack = Seq(blackDragons(4), reds(1), blacks(3), blacks(2), reds(8)).reverse),
      NormalLane(pack = Seq(greens(8), greens(7), reds(7), Rose, greens(6)).reverse)
    )),
    reserveLanes = ReserveLanes(Seq(ReserveLane(None), ReserveLane(None), ReserveLane(None))),
    drawLanes = DrawLanes(Seq(DrawLane(None), DrawLane(None), DrawLane(None)))
  )

}


object ShenzhenSolitare extends App {
  //  game.allMovablePacks.foreach(println)

  println(game.legalMoves)

  //  val move1 = game.legalMoves.head

  //  println(
  //    s"""
  //       |original game = $game
  //       |
  //      |With old moves being ${game.legalMoves}
  //       |
  //      |Now playing the move $move1
  //       |
  //    """.stripMargin)
  //
  //  val nextGame = game.playAMove(move1)
  //
  //  println(
  //    s"""
  //       |And then the game looks like = $nextGame
  //       |
  //      |With new moves being = ${nextGame.legalMoves}
  //    """.stripMargin
  //  )

  //  game.normalLanes.normalLanes.map(_.getMovablePacks).foreach(println)

  //  NormalLane(Seq()).getMovablePacks.foreach(println)

  //  println(
  //    "All movable packs => " +
  //      NormalLane(cards = Seq(blacks(5), reds(4), greens(7), greens(8), reds(9), blackDragons(1)).reverse).getAllMovablePacks)
  //
}

