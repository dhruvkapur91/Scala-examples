package scalacheck

import java.io.Serializable
import java.lang.Math.random
import java.sql.Timestamp

import org.scalacheck.{Gen, Properties}
import org.scalacheck.Prop.forAll
import org.scalacheck.Gen.posNum
import org.scalacheck.Gen.listOf
import org.scalacheck.Gen.alphaStr

import scala.util.Random

object TrySomethingElse extends Properties("bubble") {

  case class Opportunity(opportunity_key: String, serial_number_prefix: String, begin_sn: String, end_sn: String, manufacturer_code: String, labor_cost_usd: Double = 0.0)

  val x: Gen[Opportunity] = for {
    opportunityId <- Gen.alphaStr
    amount <- Gen.posNum[Int]
  } yield {
    Opportunity(opportunityId, "", "", "", "", amount)
  }

  private val n: Gen[List[Opportunity]] = Gen.listOfN(100, x)

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
    ingredientList <- Gen.someOf(slimy, crawly, creepy)
    amounts <- Gen.listOfN(ingredientList.seq.length, Gen.choose(1, 10))
    bubblesFor <- Gen.posNum[Int].suchThat(_ < 720)
  } yield {
    Spell(amounts.zip(ingredientList), bubblesFor)
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


object WorkshopProblemGenerator extends Properties("WorkshopProblemGenerator") {

  trait SearchFilter

  trait Location {
    val locationCode: String
  }

  class SpecificLocation(override val locationCode: String) extends Location

  val allLocationCodes = Seq("AD", "AE", "AF", "AG", "AI", "AL", "AM", "AN", "AO", "AQ", "AR", "AT", "AU", "AW", "AZ", "BA", "BB", "BD", "BE", "BF", "BG", "BH", "BI", "BJ", "BM", "BN", "BO", "BR", "BS", "BT", "BV", "BW", "BY", "BZ", "CA", "CC", "CF", "CG", "CH", "CI", "CK", "CL", "CM", "CN", "CO", "CR", "CU", "CV", "CX", "CY", "CZ", "DE", "DJ", "DK", "DM", "DO", "DS", "DZ", "EC", "EE", "EG", "EH", "ER", "ES", "ET", "FI", "FJ", "FK", "FM", "FO", "FR", "FX", "GA", "GB", "GD", "GE", "GF", "GH", "GI", "GK", "GL", "GM", "GN", "GP", "GQ", "GR", "GS", "GT", "GU", "GW", "GY", "HK", "HM", "HN", "HR", "HT", "HU", "ID", "IE", "IL", "IM", "IN", "IO", "IQ", "IR", "IS", "IT", "JE", "JM", "JO", "JP", "KE", "KG", "KH", "KI", "KM", "KN", "KP", "KR", "KW", "KY", "KZ", "LA", "LB", "LC", "LI", "LK", "LR", "LS", "LT", "LU", "LV", "LY", "MA", "MC", "MD", "ME", "MG", "MH", "MK", "ML", "MM", "MN", "MO", "MP", "MQ", "MR", "MS", "MT", "MU", "MV", "MW", "MX", "MY", "MZ", "NA", "NC", "NE", "NF", "NG", "NI", "NL", "NO", "NP", "NR", "NU", "NZ", "OM", "PA", "PE", "PF", "PG", "PH", "PK", "PL", "PM", "PN", "PR", "PS", "PT", "PW", "PY", "QA", "RE", "RO", "RS", "RU", "RW", "SA", "SB", "SC", "SD", "SE", "SG", "SH", "SI", "SJ", "SK", "SL", "SM", "SN", "SO", "SR", "ST", "SV", "SY", "SZ", "TC", "TD", "TF", "TG", "TH", "TJ", "TK", "TM", "TN", "TO", "TP", "TR", "TT", "TV", "TW", "TY", "TZ", "UA", "UG", "UM", "US", "UY", "UZ", "VA", "VC", "VE", "VG", "VI", "VN", "VU", "WF", "WS", "XK", "YE", "ZA", "ZM", "ZR", "ZW")
  val locations: Seq[Location] = allLocationCodes.map(new SpecificLocation(_))

  case class UserCurrentLocation(locationCode: String) extends Location

  case class UserSearchLocation(locationCode: String) extends Location

  case class BookedLocation(locationCode: String) extends Location

  case class LocationFilter(location: UserSearchLocation) extends SearchFilter

  case class PriceFilter(minimumPrice: Double, maximumPrice: Double) extends SearchFilter

  case class SurveyLog(userId: Long, sessionId: Long, currentLocation: UserCurrentLocation, searchLocation: UserSearchLocation, locationFilter: LocationFilter, priceFilter: PriceFilter) {
    private val searchLocationFormatted = if (searchLocation == null) {
      ""
    } else {
      searchLocation.locationCode
    }

    private val locationFilterFormatted: Serializable = if (locationFilter == null) {
      ""
    } else {
      locationFilter.location.locationCode
    }

    private val priceFilterFormatted = if (priceFilter == null) {
      ","
    } else {
      s"${priceFilter.minimumPrice},${priceFilter.maximumPrice}"
    }

    val toCSV = {
      s"$userId,$sessionId,${currentLocation.locationCode},$searchLocationFormatted,$locationFilterFormatted,$priceFilterFormatted"
    }
  }

  case class Booking(userId: Long, bookedLocation: BookedLocation, sessionId: Long)

  val perfectUserId = 10011L
  val sessionIfOfPerfectUser = 19L

  val lookerUserId = 89L
  val sessionOfLookerUser = 201L
  val anotherOfLookerUser = 201L

  val disappointedUserId = 92L
  val sessionOfDisappointedUser = 323L

  //  val logsOfPerfectSession = Seq(
  //    SurveyLog(perfectUserId, sessionIfOfPerfectUser, UserCurrentLocation("EG"), null, null),
  //    SurveyLog(perfectUserId, sessionIfOfPerfectUser, UserCurrentLocation("EG"), UserSearchLocation("FR"), Seq(LocationFilter(UserSearchLocation("FR")), PriceFilter(5500, 11000)))
  //  )
  //
  //  val logsOfSearchingNearbySession = Seq(
  //    SurveyLog(lookerUserId, sessionOfLookerUser, UserCurrentLocation("EG"), null, null),
  //    SurveyLog(lookerUserId, sessionOfLookerUser, UserCurrentLocation("DE"), UserSearchLocation("IN"), Seq(LocationFilter(UserSearchLocation("IN")), PriceFilter(44000, 60000))),
  //    SurveyLog(lookerUserId, sessionOfLookerUser, UserCurrentLocation("DE"), UserSearchLocation("NP"), Seq(LocationFilter(UserSearchLocation("NP")), PriceFilter(44000, 60000))),
  //    SurveyLog(lookerUserId, anotherOfLookerUser, UserCurrentLocation("DE"), UserSearchLocation("BT"), Seq(LocationFilter(UserSearchLocation("BT")), PriceFilter(44000, 60000)))
  //  )
  //
  //  val logOfDisappointedSession = Seq(
  //    SurveyLog(disappointedUserId, sessionOfDisappointedUser, UserCurrentLocation("AU"), null, null),
  //    SurveyLog(disappointedUserId, sessionOfDisappointedUser, UserCurrentLocation("AU"), UserSearchLocation("CN"), null),
  //    SurveyLog(disappointedUserId, sessionOfDisappointedUser, UserCurrentLocation("AU"), UserSearchLocation("CN"), null)
  //  )

  val bookings = Seq(
    Booking(perfectUserId, BookedLocation("FR"), sessionIfOfPerfectUser),
    Booking(lookerUserId, BookedLocation("NP"), anotherOfLookerUser)

  )

  def getRandomUniqueLongs(numberOfLongs: Int) = {
    (0 to numberOfLongs).foldLeft(Set[Long]())((otherLongs, _) => {
      var anotherPotentialLong = (random() * Long.MaxValue).toLong
      while (otherLongs.contains(anotherPotentialLong)) {
        anotherPotentialLong = (random() * Long.MaxValue).toLong
      }
      otherLongs + anotherPotentialLong
    })

  }

  val totalNumberOfRecords = 10000
  val numberOfConvertedUsers = Math.floor(totalNumberOfRecords * 0.40).toInt
  val numberOfPerfectUsers: Int = Math.floor(numberOfConvertedUsers * 0.1).toInt

  val perfectUserIds: Seq[Long] = getRandomUniqueLongs(numberOfPerfectUsers).toSeq
  val perfectUserSessions: Seq[Long] = getRandomUniqueLongs(numberOfPerfectUsers).toSeq

  for(i <- 0 to numberOfPerfectUsers){
    Random.shuffle(locations).head
  }

  val perfectUserCurrentLocations = for {
    i <- 0 to numberOfPerfectUsers
  } yield UserCurrentLocation(Random.shuffle(locations).head.locationCode)

  val perfectUserSearchLocations = for {
    i <- 0 to numberOfPerfectUsers
  } yield UserSearchLocation(Random.shuffle(locations).head.locationCode)

  val perfectUserPrices = (0 to numberOfPerfectUsers).map(_ => {
    val minimumPrice = (random() * 8500) + 2000
    val maxPrice = minimumPrice + random() * 5000
    PriceFilter(minimumPrice, maxPrice)
  })

  case class PerfectUserSurveyLogs(loginLog: SurveyLog, bookingSearch: SurveyLog)

  val perfectUserLogs = for {
    i <- 0 until numberOfPerfectUsers
  } yield PerfectUserSurveyLogs(SurveyLog(perfectUserIds(i), perfectUserSessions(i), perfectUserCurrentLocations(i), null, null, null),
    SurveyLog(perfectUserIds(i), perfectUserSessions(i), perfectUserCurrentLocations(i), perfectUserSearchLocations(i), LocationFilter(perfectUserSearchLocations(i)), perfectUserPrices(i)))

  val perfectUserSurveyLogs = perfectUserLogs
    .foldLeft(Seq[SurveyLog]())((acc, curr) => acc :+ curr.loginLog :+ curr.bookingSearch)
    .map(_.toCSV)

  perfectUserSurveyLogs.foreach(println)

  //  val perfectUserLogs : Seq[SurveyLog] = for {
  //    userIdAndSession <- perfectUserIdAndSession
  //    location <- perfectUserCurrentLocations
  //  } yield SurveyLog(userIdAndSession._1,userIdAndSession._2,location,null,null)
  //
  //  perfectUserLogs.foreach(println)

}