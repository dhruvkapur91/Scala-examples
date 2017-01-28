package xmlparsing

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
