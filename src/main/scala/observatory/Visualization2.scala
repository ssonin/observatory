package observatory

import com.sksamuel.scrimage.{Image, Pixel}
import observatory.Interaction.{tileLocation, zoomIn}
import observatory.Visualization.interpolateColor

import scala.collection.parallel.ParIterable
import scala.math.{ceil, floor}

/**
  * 5th milestone: value-added information visualization
  */
object Visualization2 extends Visualization2Interface {

  /**
    * @param point (x, y) coordinates of a point in the grid cell
    * @param d00 Top-left value
    * @param d01 Bottom-left value
    * @param d10 Top-right value
    * @param d11 Bottom-right value
    * @return A guess of the value at (x, y) based on the four known values, using bilinear interpolation
    *         See https://en.wikipedia.org/wiki/Bilinear_interpolation#Unit_Square
    */
  def bilinearInterpolation(
    point: CellPoint,
    d00: Temperature,
    d01: Temperature,
    d10: Temperature,
    d11: Temperature
  ): Temperature = {
    val (x, y) = (point.x, point.y)
    d00 * (1 - x) * (1 - y) + d10 * x * (1 - y) + d01 * (1 - x) * y + d11 * x * y
  }

  /**
    * @param grid Grid to visualize
    * @param colors Color scale to use
    * @param tile Tile coordinates to visualize
    * @return The image of the tile at (x, y, zoom) showing the grid using the given color scale
    */
  def visualizeGrid(grid: GridLocation => Temperature, colors: Iterable[(Temperature, Color)], tile: Tile): Image = {

    def unitSquare(loc: Location): (GridLocation, GridLocation, GridLocation, GridLocation) = {
      val (x0, x1, y0, y1) = (floor(loc.lon).toInt, ceil(loc.lon).toInt, ceil(loc.lat).toInt, floor(loc.lat).toInt)
      (GridLocation(y0, x0), GridLocation(y1, x0), GridLocation(y0, x1), GridLocation(y1, x1))
    }

    val locations = zoomIn(List(tile)).par.map(tileLocation)
    val pixels: ParIterable[Pixel] = for {
      loc <- locations
      (grid00, grid01, grid10, grid11) = unitSquare(loc)
      (d00, d01, d10, d11) = (grid(grid00), grid(grid01), grid(grid10), grid(grid11))
      temperature = bilinearInterpolation(new CellPoint(loc), d00, d01, d10, d11)
      color = interpolateColor(colors, temperature)
    } yield Pixel(color.red, color.green, color.blue, alpha)
    Image(tileWidth, tileHeight, pixels.toArray)
  }

}
