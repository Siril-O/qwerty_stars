package domain

class SimulationContext(val rows: Int, val columns: Int,
                        val vehicles: Int, val rides: Int,
                        val bonus: Int, val steps: Int,
                        val cars: List[Car], val riders: List[Rider]) {

  def collectResult(): String = {
    cars.map(car => car.assignedRiders.length + " " + car.assignedRiders.map(_.id).mkString(" "))
      .mkString("\n")
  }
}
