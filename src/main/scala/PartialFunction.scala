object PartialFunction extends App {
  val limitedStringTransformer : String => String = {case "X" => "transformed X"}
  println(limitedStringTransformer("X"))


  val partialStringTransformer : PartialFunction[String,String] = {case "X" => "transformed X"}
  println(partialStringTransformer("X"))
  println(partialStringTransformer.isDefinedAt("Y"))
}
