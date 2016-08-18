package tryingscalatry

object TryObject extends App {

  import scala.util.Try

  type CoffeeBeans = String
  type GroundCoffee = String

  case class Water(temperature: Int)

  type Cappuccino = String
  type Espresso = String
  type FrothedMilk = String
  type Milk = String

  def grind(beans: CoffeeBeans): GroundCoffee = s"grounded coffee of $beans"

  def heatWater(water: Water): Water = water.copy(temperature = 72)

  def frothMilk(milk: Milk): FrothedMilk = s"frothed $milk"

  def brew(coffee: GroundCoffee, heatedWater: Water): Espresso = "espresso"

  def combine(espresso: Espresso, foam: FrothedMilk): Cappuccino = "cappauccino"

  case class GrindingException(msg:String) extends Exception(msg)
  case class FrothingException(msg:String) extends Exception(msg)
  case class WaterBoilingException(msg:String) extends Exception(msg)
  case class BrewingException(msg:String) extends Exception(msg)

  def prepareCappuccino(): Try[Cappuccino] = for {
    ground <- Try(grind("arabic beans"))
    water <- Try(heatWater(Water(25)))
    espresso <- Try(brew(ground,water))
    foam <- Try(frothMilk("milk"))
  } yield combine(espresso, foam)

  print(prepareCappuccino())

}
