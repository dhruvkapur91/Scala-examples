package extractors

object MoreExtractors {

  trait User {
    def name: String

    def score: Int
  }

  class NormalUser(val name: String, val score: Int, val updateProbability: Double) extends User

  class PremiumUser(val name: String, val score: Int) extends User

  object NormalUser {
    def unapply(user: NormalUser): Option[(String, Int, Double)] = {
      Some((user.name, user.score, user.updateProbability))
    }
  }

  object PremiumUser {
    def unapply(user: PremiumUser): Option[(String, Int)] = {
      Some((user.name, user.score))
    }
  }

  val user: User = new NormalUser("dhruv", 10, 0.1)

  user match {
    case NormalUser("dhruv", _, p) =>
      if (p > 0.7) "name" else "huh"
  }

  object premiumCandidate {
    def unapply(user: NormalUser) = user.updateProbability > 0.7
  }

  def initiativeProgram(user: NormalUser) = {

  }

  user match {
    case f@premiumCandidate() => initiativeProgram(f)
    case _ => "regular news letter"
  }

  // lists :

  val aList = List(1, 2, 3)

  aList match {
    case ::(x, xs) => x
    case _ => 10
  }

}
