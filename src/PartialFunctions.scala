object PartialFunctions extends App {

  def add(x:Int,y:Int) = x + y
  println("adding two numbers 5 and 10: " + add(5,10))

  def addTwoPartialFunction = add(2,_:Int)
  println("using curried function addTwo. Add 2 to 10 is: " + addTwoPartialFunction(10))

  def curryAdd = (add _).curried
  println("using curriedAdd to add 1 and 2 is: " + curryAdd(1)(2))

  def addTwoFromCurry = curryAdd(2)
  println("using curried function addTwo. Add 2 to 10 is: " + addTwoFromCurry(10))
}
