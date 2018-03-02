package domain

class Rider(val id: Int, val start: Coordinate, val destination: Coordinate, val earliestStep: Int, val latestStep: Int) {
  var bookedBy: Car = _
}
