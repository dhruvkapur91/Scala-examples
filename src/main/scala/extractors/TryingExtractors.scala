package extractors

object TryingExtractors extends App {
  trait User {
    def name : String
  }

  class NormalUser(val name: String) extends User
  class PremiumUser(val name : String) extends User

  object NormalUser {
    def unapply(user: NormalUser): Option[String] = {
      Some(user.name)
    }
  }

  object PremiumUser {
    def unapply(user: PremiumUser): Option[String] = {
      Some(user.name)
    }
  }

  val user : User = new PremiumUser("dhruv")

  user match {
    case NormalUser(name) => name
    case PremiumUser(name) => name
  }
}
