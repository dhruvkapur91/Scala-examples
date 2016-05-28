package setoperations

object SetOperations extends App {
  val setOne = Set(1, 2, 3)
  val setTwo = Set(2, 3, 4)

  println {
    setOne.intersect(setTwo)
  }

  println {
    s"taking union ${setOne ++ setTwo} "
  }

  println {
    s"is setOne subsetOf setTwo ${setOne subsetOf setTwo} "
  }

}
