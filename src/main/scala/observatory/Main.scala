package observatory

import java.io.File

import com.sksamuel.scrimage.writer

object Main extends App {

//  val temperatures: Iterable[(Location, Temperature)] =
//    Extraction.locationYearlyAverageRecords(
//      Extraction.locateTemperatures(2015, "/stations.csv", "/2015.csv"))
//
//  val colors = List(
//    (60.0, Color(255, 255, 255)),
//    (32.0, Color(255, 0, 0)),
//    (12.0, Color(255, 255, 0)),
//    (0.0, Color(0, 255, 255)),
//    (-15.0, Color(0, 0, 255)),
//    (-27.0, Color(255, 0, 255)),
//    (-50.0, Color(33, 0, 107)),
//    (-60.0, Color(0, 0, 0)))
//
//  val image = Visualization.visualize(temperatures, colors)
//  image.output(new File("target/image2015.png"))
//
//  val tile = Interaction.tile(temperatures, colors, Tile(0, 0, 0))
//  tile.output(new File("target/tile2015.png"))

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





//  println(Interaction.tileLocation(Tile(2, 3, 2)))
//  val tiles = Interaction.zoomIn(List(Tile(0, 0, 0)), 8)
//  println(tiles.size)
//  println(tiles.toList.sortBy(t => (t.x, t.y)).take(256))
//
//  val locationsTemperatures = List((Location(45.0 , -90.0), 20.0)
//    ,(Location(45.0 , 90.0 ), 0.0)
//    ,(Location(0.0  , 0.0  ), 10.0)
//    ,(Location(-45.0, -90.0), 0.0)
//    ,(Location(-45.0, 90.0 ), 20.0)
//  )
//
//  val colorMap = List(
//    (0.0  , Color(255, 0  , 0))
//    ,(10.0 , Color(0  , 255, 0))
//    ,(20.0 , Color(0  , 0  , 255))
//  )
//
//  val temp = Visualization.predictTemperature(locationsTemperatures, Location(-27.059125784374057,-178.59375))
//  val color = Visualization.interpolateColor(colorMap, temp)
//  println(temp)
//  println(color)
//
//  val temperatures = List((Location(45.0,-90.0),20.0), (Location(45.0,90.0),0.0), (Location(0.0,0.0),10.0), (Location(-45.0,-90.0),0.0), (Location(-45.0,90.0),20.0))
//  val colors = List((0.0,Color(255,0,0)))
//  val tile = Tile(0, 0, 0)
//  val fromVisualize = Visualization.visualize(temperatures, colors)
//  println(fromVisualize)
//  fromVisualize.output(new File("target/test_image.png"))
//
//  val fromTile = Interaction.tile(temperatures, colors, tile)
//  println(fromTile)
//  fromTile.output(new File("target/test_tile.png"))
//
//
//  val pixels: IndexedSeq[(Int, Int)] =
//    for {
//      x <- 0 until 256
//      y <- 0 until 256
//    } yield (x, y)
//
//  val tiles2 = pixels.map{ case(x, y) => Tile(x + tile.x * 256, y + tile.y * 256, tile.zoom + 8)}
//  println(tiles2.size)
//  println(tiles2.take(50))
//  println(tiles == tiles2)

  val t1 = (0 to 3).flatMap(
    zoomLevel => (0 until (1 << zoomLevel)).flatMap(
      x => (0 until (1 << zoomLevel)).map(y => Tile(x, y, zoomLevel))))

  println(t1)

  val t2 = for {
    zoom <- 0 to 3
    x <- 0 until (1 << zoom)
    y <- 0 until (1 << zoom)
  } yield Tile(x, y, zoom)

  println(t2)
  println(t1 == t2)

}
