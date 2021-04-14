package observatory

import scala.math._

object Main extends App {

  val temperatures: Iterable[(Location, Temperature)] =
    Extraction.locationYearlyAverageRecords(
      Extraction.locateTemperatures(2015, "/stations.csv", "/2015.csv").take(100))

  val df = Visualization.predictTemperature(temperatures, Location(32.95, 65.567))

  println(df)

//  val (lat1, lon1) = (64.533, 12.383)
//  val (lat2, lon2) = (32.95, 65.567)
//  val d = acos(sin(lat1) * sin(lat2) + cos(lat1) * cos(lat2) * cos(lon2 - lon1)) * 6371

//  val (lat1, lon1) = (56.153, 40.426)
//  val (lat2, lon2) = (56.984, 40.967)
//  val (lat1, lon1) = (64.533, 12.383)
//  val (lat2, lon2) = (32.95, 65.567)
//  val (lat1r, lon1r, lat2r, lon2r) = (lat1 * Pi / 180, lon1 * Pi / 180, lat2 * Pi / 180, lon2 * Pi / 180)
//  val d = acos(sin(lat1r) * sin(lat2r) + cos(lat1r) * cos(lat2r) * cos(abs(lon1r - lon2r))) * 6371
//
//  println(d)

}
