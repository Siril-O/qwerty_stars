package main

import services.{DataMapper, SimulationRunner}

object Main {

  def main(args: Array[String]): Unit = {
    try {
      val mapper: DataMapper = new DataMapper("e_high_bonus.in")
      val simulationContext = mapper.initSimulationContextFromInputData()
      val simulationRunner: SimulationRunner = new SimulationRunner()

      profileTime {
        simulationRunner.runSimulation(simulationContext)
      }
      println(simulationContext.collectResult())
      mapper.writOutputToFile(simulationContext, "e")
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
