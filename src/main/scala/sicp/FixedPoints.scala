package sicp

import java.lang.Math.abs

object Sicp {

  def average(a: Double, b: Double) = (a + b) / 2

  def closeEnough(a: Double, b: Double, tolerance: Double) = abs(a - b) < tolerance

  def findRootByHalfInterval(function: (Double) => Double, negativePoint: Double, positivePoint: Double, tolerance: Double = 0.01): Double = {
    val middlePoint = average(negativePoint, positivePoint)
    if (closeEnough(negativePoint, positivePoint, tolerance)) {
      middlePoint
    } else {
      val valueAtMiddlePoint = function(middlePoint)
      if (valueAtMiddlePoint < 0) {
        findRootByHalfInterval(function, middlePoint, positivePoint, tolerance)
      } else {
        findRootByHalfInterval(function, negativePoint, middlePoint, tolerance)
      }
    }
  }

  def fixedPoint(f: Double => Double, x: Double, tolerance: Double = 0.01): Double = {
    if (closeEnough(x, f(x), tolerance)) {
      x
    } else {
      fixedPoint(f, f(x), tolerance)
    }
  }

}

object EightQueens extends App {

  type Row = Int
  type Column = Int

  case class Position(row: Row, column: Column) {

    def diagonalPositions() = {

      def isLegalPosition(rowDiff: Int, columnDiff: Int) = row + rowDiff >= 1 && column + columnDiff >= 1

      val upperLeftDiagonals: Seq[Position] = for {
        rowDiff <- 1 to 8
        columnDiff <- 1 to 8
        newPosition = Position(row - rowDiff, column - columnDiff)
        if rowDiff == columnDiff && isLegalPosition(-rowDiff, -columnDiff)
      } yield newPosition

      val upperRightDiagonals: Seq[Position] = for {
        rowDiff <- 1 to 8
        columnDiff <- 1 to 8
        newPosition = Position(row - rowDiff, column + columnDiff)
        if rowDiff == columnDiff && isLegalPosition(-rowDiff, columnDiff)
      } yield newPosition

      val lowerRightDiagonals: Seq[Position] = for {
        rowDiff <- 1 to 8
        columnDiff <- 1 to 8
        newPosition = Position(row + rowDiff, column + columnDiff)
        if rowDiff == columnDiff && isLegalPosition(rowDiff, columnDiff)
      } yield newPosition

      val lowerLeftDiagonals: Seq[Position] = for {
        rowDiff <- 1 to 8
        columnDiff <- 1 to 8
        newPosition = Position(row + rowDiff, column - columnDiff)
        if rowDiff == columnDiff && isLegalPosition(rowDiff,-columnDiff)
      } yield newPosition

      upperLeftDiagonals ++ lowerLeftDiagonals ++ upperRightDiagonals ++ lowerRightDiagonals
    }

    def isConflicting(other: Position) = other.row == row || other.column == column || diagonalPositions().contains(other)

    def isNotConflicting(other: Position) = !isConflicting(other)
  }

  implicit class RichPositions(positions: Seq[Position]) {
    def nonConflicting(other: Seq[Position]) = {
      positions.filter(potentialPosition => other.forall(_.isNotConflicting(potentialPosition)))
    }
  }

  def placeQueens(positions: Seq[Position]): Seq[Position] = {
    def _placeQueens(occupiedPositions: Seq[Position] = Seq.empty, otherTriedPositions : Seq[Position] = Seq.empty): Seq[Position] = {
      if (occupiedPositions.size == 8) {
        occupiedPositions
      } else {
        val availablePositions = positions.nonConflicting(occupiedPositions) diff otherTriedPositions
        if (availablePositions.isEmpty) {
          println(occupiedPositions)
          _placeQueens(occupiedPositions.take(occupiedPositions.length - 1),Seq(occupiedPositions.last))
        } else {
          _placeQueens(occupiedPositions :+ availablePositions.head)
        }
      }
    }
    _placeQueens()
  }

  val allPositions = for {
    row <- 1 to 8
    column <- 1 to 8
  } yield Position(row, column)

  println(placeQueens(allPositions))
}