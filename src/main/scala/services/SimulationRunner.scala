package services

import domain.{Car, Coordinate, Rider, SimulationContext}

import scala.collection.mutable

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
      val freeCars = simulationContext.cars.filter(car => car.bookedBy == null)
      if (freeCars.nonEmpty) {
        unassignedRiders.foreach(rider => {
          val closestCar = findClosestCar(freeCars, rider, true)
          if (closestCar.isDefined) {
            assignCarToRider(closestCar.get, rider)
          }
        })
      }
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

    val freeCars = cars.filter(car => car.bookedBy == null)
    if (freeCars.nonEmpty) {
      val compValues: mutable.HashMap[Rider, Int] = new mutable.HashMap[Rider, Int]()
      riders.foreach(rider => compValues.put(rider, comp(freeCars, rider)))
      riders.sortWith((rider1: Rider, rider2: Rider) => {
        compValues.get(rider1).get < compValues.get(rider2).get
      })
    } else {
      riders
    }
  }

  private def comp(cars: List[Car], rider: Rider): Int = {
    val closestCar = findClosestCar(cars, rider, false)
    if (closestCar.isDefined) rider.earliestStep - calcDistance(closestCar.get.coordinates, rider.start) else 0
  }

  private def findClosestCar(cars: List[Car], rider: Rider, requiresFiltering: Boolean): Option[Car] = {
    if (requiresFiltering) {
      val freeCars = cars.filter(car => car.bookedBy == null)
      if (freeCars.isEmpty) None else Option(freeCars.minBy((car: Car) => calcDistance(car.coordinates, rider.destination)))
    } else {
      if (cars.isEmpty) None else Option(cars.minBy((car: Car) => calcDistance(car.coordinates, rider.destination)))
    }

  }

  private def calcDistance(coordinate1: Coordinate, coordinate2: Coordinate): Int = {
    Math.abs(coordinate1.x - coordinate2.x) + Math.abs(coordinate1.y - coordinate2.y)
  }
}