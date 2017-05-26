package fibonnaci

case class Adult() {
  def reproduce = Child()
}

case class Child() {
  def grow = Adult()
}

case class Population(adults: Seq[Adult], children: Seq[Child]) {
  def evolve = Population(adults ++ children.map(_.grow), adults.map(_.reproduce))
  val stats = Map(
    "Adults" -> adults.length,
    "Children" -> children.length,
    "Total" -> (adults.length + children.length)
  )
}

object FibonnaciSimulation extends App {
  val adam = Adult()

  def log(population: Population) = {
    println(population.stats)
    population
  }

  def evolve(population: Population) = population.evolve

  def workflow = evolve _ andThen log _

  def simulateEvolution(population: Population, numberOfIterations: Int): Population = {
    if (numberOfIterations <= 0) population else simulateEvolution(workflow(population),numberOfIterations-1)
  }

  simulateEvolution(Population(Seq(adam),Seq.empty),20)

}
