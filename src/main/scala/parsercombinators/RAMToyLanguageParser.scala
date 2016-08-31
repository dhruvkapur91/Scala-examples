package parsercombinators

import java.io.Serializable

import scala.util.parsing.combinator.RegexParsers

class RAMToyLanguageParser extends RegexParsers {
  type digit = String
  type letter = String
  type equals = String
  type zero = String
  type letterOrDigit = String
  type lettersOrDigits = List[String]
  type variable = ~[letter, lettersOrDigits]
  type check = ~[~[variable, equals], zero]

  def digit: Parser[digit] = "[0-9]".r

  def letter: Parser[letter] = "[a-z]|[A-Z]".r

  def variable: Parser[variable] = letter ~ rep(letter | digit)

  def check: Parser[check] = variable ~ "=" ~ "0"

  def outputCommand = "write" ~ variable

  def inputCommand = "read" ~ variable

  def assignmentCommand = variable ~ ("++" | "--")

  def command: Parser[~[Serializable, Serializable]] = assignmentCommand | inputCommand | outputCommand | whileCommand | ifCommand | failure("unexpected symbol")

  def commands = rep(command)

  def ifCommand = "if" ~ check ~ "then" ~ commands ~ rep("else" ~ commands) ~ "end" // need a way to put optional else...

  def whileCommand = "while" ~ check ~ "do" ~ commands ~ "end"

  def program = commands

  def parseDigit(text: String) = parseAll(digit, text)

  def parseLetter(text: String) = parseAll(letter, text)

  def parseVariable(text: String) = parseAll(variable, text)

  def parseCheck(text: String) = parseAll(check, text)

  def parseOutputCommand(text: String) = parseAll(outputCommand, text)

  def parseInputCommand(text: String) = parseAll(inputCommand, text)

  def parseWhile(text : String) = parseAll(whileCommand,text)

  def parseProgram(text : String) = parseAll(program,text)
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
    s"Should match a check expression... ${
      new RAMToyLanguageParser().parseCheck(
        """
          |pqr123 = 0
        """.stripMargin)
    } "
  }

//  println {
//    s"Lets try while... ${
//      new RAMToyLanguageParser().parseWhile(
//        """
//          |while pqr123 = 0  do read abc end
//        """.stripMargin
//      )
//    }"
//  }

  println {
    s"Should parse the whole program... ${
      new RAMToyLanguageParser().parseProgram(
        """
          |read x  read y
        """.stripMargin)
    }"
  }
}
