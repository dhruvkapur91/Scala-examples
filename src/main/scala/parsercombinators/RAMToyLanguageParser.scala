package parsercombinators

import scala.util.parsing.combinator.RegexParsers

class RAMToyLanguageParser extends RegexParsers {
  type digit = String
  type letter = String
  type equals = String
  type zero = String
  type letterOrDigit = String
  type lettersOrDigits = List[String]
  type variable = ~[letter,lettersOrDigits]
  type check = ~[~[variable,equals],zero]

  def digit: Parser[digit] = "[0-9]".r

  def letter: Parser[letter] = "[a-z]|[A-Z]".r

  def variable: Parser[variable] = letter ~ rep(letter | digit)

  def check: Parser[check] = variable ~ "=" ~ "0"

  def parseDigit(text: String) = parseAll(digit, text)

  def parseLetter(text: String) = parseAll(letter, text)

  def parseVariable(text: String) = parseAll(variable, text)

  def parseCheck(text: String) = parseAll(check, text)
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

  println {
    s"Should match a check expression... ${new RAMToyLanguageParser().parseCheck(
      """
        |pqr123 = 0
      """.stripMargin)} "
  }
}
