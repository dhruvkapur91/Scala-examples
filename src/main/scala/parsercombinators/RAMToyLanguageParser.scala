package parsercombinators

import scala.util.parsing.combinator.RegexParsers

class RAMToyLanguageParser extends RegexParsers {
  def digit: Parser[String] = "[0-9]".r

  def letter: Parser[String] = "[a-z]|[A-Z]".r

  def variable: Parser[~[String, List[String]]] = letter ~ rep(letter | digit)

  def parseDigit(text: String) = parseAll(digit, text)

  def parseLetter(text: String) = parseAll(letter, text)

  def parseVariable(text: String) = parseAll(variable, text)
}

object RAMToyLanguageParser extends App {
  println {
    s"Should match a digit ${new RAMToyLanguageParser().parseDigit("1")} "
  }

  println {
    s"Should match a letter ${new RAMToyLanguageParser().parseLetter("A")} "
  }

  println {
    s"Should match a variable of 2 len char ${new RAMToyLanguageParser().parseVariable("a1")} "
  }

  println {
    s"Should match a variable of arb len char ${new RAMToyLanguageParser().parseVariable("a1l343llsdsd2")} "
  }
}
