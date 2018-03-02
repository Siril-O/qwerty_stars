package domain

import scala.collection.mutable.ListBuffer

class Car(val id: Int, var coordinates: Coordinate) {
  var bookedBy: Rider = _
  var bookedStepsLeft: Int = _
  val assignedRiders: ListBuffer[Rider] = ListBuffer[Rider]()

  def this(id: Int) = this(id, new Coordinate(0, 0))

}
