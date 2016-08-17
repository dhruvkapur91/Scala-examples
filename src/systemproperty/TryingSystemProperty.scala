package systemproperty

object TryingSystemProperty extends App {
  args foreach println

  println(System.getProperty("hello"))
}
