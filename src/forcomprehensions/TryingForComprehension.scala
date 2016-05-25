package forcomprehensions

object TryingForComprehension extends App {
  // Consider :

  println {
    for {
      x <- 1 to 10
    } yield x
  }

  /*
  So the above for comprehension converts into
  val someVal = 1 to 10 map { x => x }
   */

  // Lets try it out

  println {
    1 to 10 map { x => x }
  }

  /*
  What about the if...
   */

  println {
    for {
      x <- 1 to 10
      if x > 5
    } yield x
  }

  /*
  This should probably translate into:
  1 to 10 map {x => x} filter {x => x > 5}
   */

  // Lets try it out

  println {
    1 to 10 map { x => x } filter { x => x > 5 }
  }

  // What happens if we change the yield value to some function of the variables...

  println {
    for {
      x <- 1 to 10
    } yield x * 2
  }

  /*
  This should have become:
  1 to 10 map { x => x * 2 }
   */

  println {
    1 to 10 map { x => x * 2 }
  }

  // Now lets try with 2 variables...

  println {
    for {
      x <- 1 to 5
      y <- 1 to 5
    } yield x + y
  }

  /*
  This should translate into...
  1 to 5 flatMap { y => 1 to 5 }
   */

  println {
    val y = 1 to 5
    val x = 1 to 5
    y flatMap { y => x map { x => x + y } }
  }

  // One more time with filtering

  println {
    for {
      x <- 1 to 5
      y <- 1 to 5
      if x + y > 2
    } yield x + y
  }

  /*
  This thing converts to...
  y flatMap { y => x map { x => x+ y} } filter { z => z > 2 }
  Or
  y flatMap { y => x map { x => x + y} filter { z => z > 2 } }
   */

  println {
    val y = 1 to 5
    val x = 1 to 5
    y flatMap { y => x map { x => x + y } } filter { z => z > 2 }
  }

  // Or

  println {
    val y = 1 to 5
    val x = 1 to 5
    y flatMap { y => x map { x => x + y } filter { z => z > 2 } }
  }

  // With result being function of the paramters

  println {
    for {
      x <- 1 to 5
      y <- 1 to 5
      if x + y > 2
    } yield (x + y) * 2
  }

  /*
  This being...
  y flatMap { y => x map { x => (x + y) } filter { z => z > 2 } map { x => x*2 } }
   */

  println {
    val y = 1 to 5
    val x = 1 to 5
    y flatMap { y => x map { x => x + y } filter { z => z > 2 } map { x => x * 2 } }
  }

  // What about 3 variables...

  println {
    for {
      x <- 1 to 3
      y <- 1 to 3
      z <- 1 to 3
    } yield x + y + z
  }

  /*
  This translates into...
  z flatMap { z => y flatMap { y => x map { x => x + y + z } } }
   */

  println {
    val x = 1 to 3
    val y = 1 to 3
    val z = 1 to 3
    z flatMap { z => y flatMap { y => x map { x => x + y + z } } }
  }


}
