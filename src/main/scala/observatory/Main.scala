package observatory

import java.io.File

import com.sksamuel.scrimage.writer

object Main extends App {

  val temperatures: Iterable[(Location, Temperature)] =
    Extraction.locationYearlyAverageRecords(
      Extraction.locateTemperatures(2015, "/stations.csv", "/2015.csv"))
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

//  val scale = List((1.0,Color(255,0,0)), (2.0,Color(0,0,255)), (3.0,Color(0,255,0)))
//  val temperature = 2.5
//  val color = Visualization.interpolateColor(scale, temperature)
//  println(color)
//

//  val temperatures = List((Location(45.0,-90.0),0.0), (Location(-45.0,0.0),6.28630864814906))
  val colors = List(
    (60.0, Color(255, 255, 255)),
    (32.0, Color(255, 0, 0)),
    (12.0, Color(255, 255, 0)),
    (0.0, Color(0, 255, 255)),
    (-15.0, Color(0, 0, 255)),
    (-27.0, Color(255, 0, 255)),
    (-50.0, Color(33, 0, 107)),
    (-60.0, Color(0, 0, 0)))

  val image = Visualization.visualize(temperatures, colors)
  image.output(new File("target/image.png"))

}
