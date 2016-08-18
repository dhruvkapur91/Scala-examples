object PartialFunctionAndLSP extends App {
  private val partialStringTransformer: PartialFunction[String,String] = {case "x" => "Y"}
  val fullStringTransformer = (x : String) => x.toUpperCase

  def transform_apple(f : Function[String,String]) = println(f("apple"))

  transform_apple(partialStringTransformer)
}
