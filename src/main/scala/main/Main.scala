package main

import services.{InputDataMapper, SimulationRunner}

object Main {

  def main(args: Array[String]): Unit = {
    try {
      val mapper: InputDataMapper = new InputDataMapper("d_metropolis.in")
      val simulationContext = mapper.initSimulationContextFromInputData()
      val simulationRunner: SimulationRunner = new SimulationRunner()

      profileTime{simulationRunner.runSimulation(simulationContext)}
      println(simulationContext.collectResult())
    } catch {
      case exc: Exception => println(exc)
    }
  }

  def profileTime(execution: => Unit): Unit = {
    val t0 = System.currentTimeMillis()
    execution
    val t1 = System.currentTimeMillis()
    println("Elapsed time: " + (t1 - t0) + "ml")
  }

}
