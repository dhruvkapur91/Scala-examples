package parsercombinators

import scala.util.parsing.combinator.RegexParsers


class BinaryDigitParser extends RegexParsers {
  def digit: Parser[String] = "0" | "1"

  def binaryNumber = digit <~ rep(digit) ^^ { case x => 1}

  def parse(text: String): ParseResult[~[String, List[String]]] = parseAll(binaryNumber, text)
}

object Something extends App {
  println {
    new BinaryDigitParser().parse("0101010")
  }
}