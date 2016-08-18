package parsercombinators

import scala.util.parsing.combinator.RegexParsers


class BinaryDigitParser extends RegexParsers {
  def digit: Parser[String] = "0" | "1"

  def binaryNumber: Parser[~[String, List[String]]] = digit ~ rep(digit)

  def parse(text: String) = parseAll(binaryNumber, text)
}

object Something extends App {
  println {
    new BinaryDigitParser().parse("0101010")
  }
}