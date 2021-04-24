package observatory

import com.sksamuel.scrimage.{Image, Pixel}
import observatory.Visualization.visualize

import scala.annotation.tailrec
import scala.math.{Pi, atan, sinh, toDegrees => degrees}

/**
  * 3rd milestone: interactive visualization
  */
object Interaction extends InteractionInterface {

  /**
    * @param tile Tile coordinates
    * @return The latitude and longitude of the top-left corner of the tile,
    *         as per http://wiki.openstreetmap.org/wiki/Slippy_map_tilenames
    */
  def tileLocation(tile: Tile): Location = {
    val lat = degrees(atan(sinh(Pi * (1 - tile.y * 2.0 / (1 << tile.zoom)))))
    val lon = tile.x * 360.0 / (1 << tile.zoom) - 180
    Location(lat, lon)
  }

  /**
    * @param temperatures Known temperatures
    * @param colors Color scale
    * @param tile Tile coordinates
    * @return A 256×256 image showing the contents of the given tile
    */
  def tile(temperatures: Iterable[(Location, Temperature)], colors: Iterable[(Temperature, Color)], tile: Tile): Image = {
    val locations = zoomIn(List(tile)).par.map(tileLocation)
    visualize(locations)(tileWidth, tileHeight, alpha)(temperatures, colors)
  }

  @tailrec
  def zoomIn(tiles: Iterable[Tile], n: Int = 8): Iterable[Tile] = {
    require(n >= 0, "Number of times to zoom in cannot be negative")
    if (n == 0) tiles.toSeq.sortBy(tile => (tile.y, tile.x))
    else zoomIn(tiles.flatMap(_.zoomIn), n - 1)
  }

  /**
    * Generates all the tiles for zoom levels 0 to 3 (included), for all the given years.
    * @param yearlyData Sequence of (year, data), where `data` is some data associated with
    *                   `year`. The type of `data` can be anything.
    * @param generateImage Function that generates an image given a year, a zoom level, the x and
    *                      y coordinates of the tile and the data to build the image from
    */
  def generateTiles[Data](yearlyData: Iterable[(Year, Data)], generateImage: (Year, Tile, Data) => Unit): Unit = {
    for {
      zoom <- (0 to 3).par
      x <- 0 until (1 << zoom)
      y <- 0 until (1 << zoom)
      (year, data) <- yearlyData
    } generateImage(year, Tile(x, y, zoom), data)
  }

}
