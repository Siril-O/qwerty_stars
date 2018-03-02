package main

import services.{DataMapper, SimulationRunner}

object Main {

  def main(args: Array[String]): Unit = {
    try {
      val mapper: DataMapper = new DataMapper("b_should_be_easy.in")
      val simulationContext = mapper.initSimulationContextFromInputData()
      val simulationRunner: SimulationRunner = new SimulationRunner()

      profileTime {
        simulationRunner.runSimulation(simulationContext)
      }
      println(simulationContext.collectResult())
      mapper.writOutputToFile(simulationContext, "b")
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
