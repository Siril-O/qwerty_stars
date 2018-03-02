package main

import domain.SimulationContext
import services.InputDataMapper

object Main {

  def main(args: Array[String]): Unit = {

    try {
      val mapper: InputDataMapper = new InputDataMapper("a_example.in")
      val inputData = mapper.mapInputData()
      println(inputData)
    } catch {
      case exc: Exception => {
        println(exc)
      }
    }
  }

}
