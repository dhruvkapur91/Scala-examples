package parsercombinators

import scala.util.parsing.combinator.RegexParsers

class RAMToyLanguageParser extends RegexParsers {
  def digit: Parser[String] = "[0-9]".r

//  def letter :

  def parseDigit(text: String) = parseAll(digit,text)
}

object RAMToyLanguageParser extends App {
  println {
    s"Should match a digit ${new RAMToyLanguageParser().parseDigit("1")} "
  }
}
