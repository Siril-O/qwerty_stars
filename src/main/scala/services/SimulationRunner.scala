package services

import domain.{Car, Coordinate, Rider, SimulationContext}

class SimulationRunner {

  def runSimulation(simulationContext: SimulationContext): Unit = {
    (0 until simulationContext.steps).toList.foreach(step => {
      profileSteps(step, () => performStepCalculations(simulationContext, step))
    })
  }

  private def profileSteps(step: Int, stepLogic: () => Unit): Unit = {
    println(s"Step :${step}")
    stepLogic()
  }

  private def performStepCalculations(simulationContext: SimulationContext, step: Int): Unit = {
    val riders = sortRidersByPriority(simulationContext.riders, simulationContext.cars)
    val unassignedRiders = riders.filter(_.bookedBy == null)
    if (unassignedRiders.nonEmpty) {
      unassignedRiders.foreach(rider => {
        val closestCar = findClosestCar(simulationContext.cars, rider)
        if (closestCar.isDefined) {
          assignCarToRider(closestCar.get, rider)
        }
      })
    }
    reduceBookedStepForCars(riders, simulationContext.cars)
  }


  private def reduceBookedStepForCars(riders: List[Rider], cars: List[Car]): Unit = {
    val assignedCars = cars.filter(_.bookedBy != null)
    if (assignedCars.nonEmpty) {
      cars.foreach(updateCarInfo)
    }

    def updateCarInfo(car: Car): Unit = {
      car.bookedStepsLeft -= 1
      if (car.bookedStepsLeft == 0) {
        car.coordinates.x = car.bookedBy.destination.x
        car.coordinates.y = car.bookedBy.destination.y
        car.bookedBy = null
      }
    }
  }


  private def assignCarToRider(car: Car, rider: Rider): Unit = {
    val distanceToStart = calcDistance(car.coordinates, rider.destination)
    val rideDistance = calcDistance(rider.start, rider.destination)
    car.bookedStepsLeft = distanceToStart + rideDistance
    car.assignedRiders += rider
    car.bookedBy = rider
    rider.bookedBy = car
  }


  private def sortRidersByPriority(riders: List[Rider], cars: List[Car]): List[Rider] = {
    def comp(cars: List[Car], rider: Rider): Int = {
      val closestCar = findClosestCar(cars, rider)
      if (closestCar.isDefined) rider.earliestStep - calcDistance(closestCar.get.coordinates, rider.start) else 0
    }
    riders.sortWith((rider1: Rider, rider2: Rider) => {
      comp(cars, rider1) < comp(cars, rider2)
    })
  }

  private def findClosestCar(cars: List[Car], rider: Rider): Option[Car] = {
    val freeCars = cars.filter(car => car.bookedBy == null)
    if (freeCars.isEmpty) None else Option(freeCars.minBy((car: Car) => calcDistance(car.coordinates, rider.destination)))
  }

  private def calcDistance(coordinate1: Coordinate, coordinate2: Coordinate): Int = {
    Math.abs(coordinate1.x - coordinate2.x) + Math.abs(coordinate1.y - coordinate2.y)
  }
}