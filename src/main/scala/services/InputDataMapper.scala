package services

import domain.{Car, Coordinate, Rider, SimulationContext}

import scala.collection.mutable.ListBuffer
import scala.io.Source

class InputDataMapper(fileName: String) {

  def initSimulationContextFromInputData(): SimulationContext = {
    val lines: ListBuffer[String] = readFromFile(fileName)
    var index = 0
    val riders: List[Rider] = lines.drop(1).map(el => {
      val rider: Rider = mapRider(el, index)
      index += 1
      rider
    }).toList
    mapSimulationContext(lines.head, riders)
  }

  private def initCars(amount: Int): List[Car] = {
    (0 to amount).toList.map(new Car(_))
  }

  private def readFromFile(file: String): ListBuffer[String] = {
    Source.fromFile(InputDataMapper.baseFolder + file).getLines.toList.to[ListBuffer]
  }

  private def mapRider(riderStr: String, id: Int): Rider = {
    val values: Array[Int] = extractList(riderStr)
    new Rider(id, new Coordinate(values(0), values(1)), new Coordinate(values(2), values(3)), values(4), values(5))
  }


  private def extractList(riderStr: String) = {
    riderStr.split(" ").map(_.toInt)
  }

  private def mapSimulationContext(simContextStr: String, riders: List[Rider]): SimulationContext = {
    //todo add guava
    //    if(simContextStr == null) return None
    val values: Array[Int] = extractList(simContextStr)
    val cars = initCars(values(2))
    new SimulationContext(values(0), values(1), values(2), values(3), values(4), values(5), cars, riders)
  }
}

object InputDataMapper {
  private val baseFolder = new java.io.File(".").getCanonicalPath + "\\resources\\inputData\\"
}

