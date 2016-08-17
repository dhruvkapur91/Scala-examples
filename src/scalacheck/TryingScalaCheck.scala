package scalacheck

import org.scalacheck.{Gen, Properties}
import org.scalacheck.Prop.forAll
import org.scalacheck.Gen.posNum
import org.scalacheck.Gen.listOf
import org.scalacheck.Gen.alphaStr

object TrySomethingElse extends Properties("bubble") {

  case class Opportunity(opportunity_key: String, serial_number_prefix: String, begin_sn: String, end_sn: String, manufacturer_code: String, labor_cost_usd: Double = 0.0)

  val x: Gen[Opportunity] = for {
    opportunityId <- Gen.alphaStr
    amount <- Gen.posNum[Int]
  } yield {
    Opportunity(opportunityId,"","","","",amount)
  }

  private val n: Gen[List[Opportunity]] = Gen.listOfN(100,x)

  n.sample.foreach(println(_))
//
//
//  val opportunities = Seq(
//    Opportunity("OPP Key 1", "Serial Prefix", "Begin Sn", "End Sn", "Manufacturer Code", labor_cost_usd = 100.0),
//    Opportunity("OPP Key 2", "Serial Prefix", "Begin Sn", "End Sn", "Manufacturer Code", labor_cost_usd = 200.0)
//  )
//
//  println {
//    opportunities.map(_.labor_cost_usd).sum == 300.0
//  }
}

object SomeSpecWithActualFancyGenerators extends Properties("bubble") {

  trait Ingredient

  case object slimy extends Ingredient

  case object crawly extends Ingredient

  case object creepy extends Ingredient

  case class Spell(ingredients: List[(Int, Ingredient)], boilingTime: Int)

  for {
    ingredientList <- Gen.someOf(slimy,crawly,creepy)
    amounts <- Gen.listOfN(ingredientList.seq.length,Gen.choose(1,10))
    bubblesFor <- Gen.posNum[Int].suchThat(_ < 720)
  } yield {
    Spell(amounts.zip(ingredientList),bubblesFor)
  }
}

object SomeSpecWithFancyGenerators extends Properties("bubble") {
  // choose, oneOf, someOf

  // choose picks something from a range : like age, a month, a day.
  // choose picks randomly from the range. if bias is needed there are other methods.

  // oneOf will pick from a specific set of values.

  // someOf will make many list of different sizes

  // someOf, listOf are higher order gens... so they can be used in conduction and possibilities explode.

  // if we use the basic out of the box gens then we have power to we get shrinking free :)
}

object SomeSpecWithExplicitGenerators extends Properties("Bubble") {
  // This one is closer to the exceptions and passes.

  def spell(words: List[String], mysticNumber: Int) = {
    mysticNumber.toString + words.mkString + "toil" + "trouble"
  }

  property("bubble") = forAll(listOf(alphaStr), posNum[Int]) { (a, b) =>
    spell(a, b).length == Math.ceil(Math.log10(b + 1)) + a.map(_.length).sum + 11
  }
}

object SomeSpecWithoutExplicitGenerators extends Properties("Bubble") {
  // This one fails for trying too much of edge scenes

  def spell(words: List[String], mysticNumber: Int) = {
    mysticNumber.toString + words.mkString + "toil" + "trouble"
  }

  property("bubble") = forAll { (a: List[String], b: Int) =>
    spell(a, b).length == Math.ceil(Math.log10(b + 1)) + a.map(_.length).sum + 11
  }
}


