package xmlparsing

import java.net.URL

import scala.io.Source

object XMLExamples extends App {
  val somePage =
    <html>
      <head>
        <title>Hello Scala XML</title>
      </head>
      <body>
        <h1>Hello World</h1>
        <p><a href="http://google.com">Something</a></p>
      </body>
    </html>

  println(somePage)
}

object PrintingXML extends App {
  val xmltest = <car make="Hyundai">Verna</car>
  println(xmltest)
}

object GenerateXML extends App {
  val cars = Map("Hyundai" -> "Verna", "Nissan" -> "Micra")
  def createCars() = {
    cars.map {
      case (key,value) => <car make={key}>{value}</car>
    }
  }
  println(<cars>{createCars()}</cars>)
}

object ParsingXMLFromService extends App {
  val sourceUrl = new URL("https://www.google.co.in/search?q=weather+forecast+new+delhi&cad=h")
  val response = Source.fromURL(sourceUrl).mkString
  println(response)
}