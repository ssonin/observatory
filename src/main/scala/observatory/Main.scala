package observatory

import scala.math._

object Main extends App {

//  val temperatures: Iterable[(Location, Temperature)] =
//    Extraction.locationYearlyAverageRecords(
//      Extraction.locateTemperatures(2015, "/stations.csv", "/2015.csv").take(100))
//
//  val df = Visualization.predictTemperature(temperatures, Location(32.95, 65.567))
//  println(df)

//  Color(128,0,128) (scale = List((0.0,Color(255,0,0)), (1.0,Color(0,0,255))), value = 0.5)

//  val scale = List((0.0,Color(255,0,0)), (1.0,Color(0,0,255)))
//  val temperature = 0.5
//  val scale = List((1.0,Color(255,0,0)), (2.0,Color(0,0,255)), (3.0,Color(0,255,0)))
//  val temperature = 1.5

//  val scale = List((1.0,Color(255,0,0)), (2.0,Color(0,0,255)), (3.0,Color(0,255,0)))
//  val temperature = 1.25

  val scale = List((1.0,Color(255,0,0)), (2.0,Color(0,0,255)), (3.0,Color(0,255,0)))
  val temperature = 2.5

  val color = Visualization.interpolateColor(scale, temperature)
  //List((1.0,Color(255,0,0)), (2.0,Color(0,0,255)), (3.0,Color(0,255,0)))

  println(color)
}
