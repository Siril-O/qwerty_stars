package domain
import scala.collection.mutable.ListBuffer

class Car(id: Int, var coordinates: Coordinate) {
  var bookedBy: Rider = _
  var bookedStepsLeft: Int = _
  val assignedRiders: ListBuffer[Rider] = ListBuffer[Rider]()

}
