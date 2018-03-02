package domain

class Rider(id: Int, start: Coordinate, destination: Coordinate, earliestStep: Int, latestStep: Int) {
  var bookedBy: Car = _
}
