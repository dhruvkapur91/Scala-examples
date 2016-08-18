package extractors

import java.net.URL

object URLExtractor extends App {

  object urlTypeString {
    def unapply(s: String) = {
      s.startsWith("http")
    }
  }

  def isAUrl(url: URL) = {
    "yes its a url"
  }

  println {
    "http://www.google.com" match {
      case url@urlTypeString() => isAUrl(new URL(url))
      case _ => "not a url"
    }

  }

}
