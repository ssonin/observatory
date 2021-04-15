package observatory

import com.sksamuel.scrimage.{Image, Pixel}

import scala.annotation.tailrec
import scala.math.{abs, acos, cos, pow, round, sin, toRadians}

/**
  * 2nd milestone: basic visualization
  */
object Visualization extends VisualizationInterface {

  private val iwdPower = 2
  private val earthRadius = 6371
  private val distancePrecision = 1

  /**
    * @param temperatures Known temperatures: pairs containing a location and the temperature at this location
    * @param target Location where to predict the temperature
    * @return The predicted temperature at `location`
    */
  def predictTemperature(temperatures: Iterable[(Location, Temperature)], target: Location): Temperature = {
    val parTemperatures = temperatures.par
    val equalToTarget = parTemperatures.filter{case (loc, _) => loc == target}
    if (equalToTarget.isEmpty) {
      val distances = parTemperatures.map{case(loc, temp) => (greatCircleDistance(loc, target), temp)}
      val closeOnes = distances.filter{case (distance, _) => distance < distancePrecision}
      if (closeOnes.isEmpty) {
        val (nom, denom) =
          distances.map{case (distance, temp) => idwTerm(distance, temp)}
            .aggregate((0.0, 0.0))(
              {case ((nom, denom), term) => (nom + term._1, denom + term._2)},
              {case ((nomL, denomL), (nomR, denomR)) => (nomL + nomR, denomL + denomR)})
        nom / denom
      } else closeOnes.head._2
    } else equalToTarget.head._2
  }

  def idwTerm(distance: Double, temp: Temperature): (Double, Double) = {
    val coeff = pow(distance, iwdPower)
    (temp / coeff, 1 / coeff)
  }

  def greatCircleDistance(from: Location, to: Location): Double = {
    val (fromR, toR) = (radians(from), radians(to))
    acos(sin(fromR.lat) * sin(toR.lat) + cos(fromR.lat) * cos(toR.lat) * cos(abs(fromR.lon - toR.lon))) * earthRadius
  }

  def radians(loc: Location): Location = Location(toRadians(loc.lat), toRadians(loc.lon))

  /**
    * @param points Pairs containing a value and its associated color
    * @param value The value to interpolate
    * @return The color that corresponds to `value`, according to the color scale defined by `points`
    */
  def interpolateColor(points: Iterable[(Temperature, Color)], value: Temperature): Color = {
    require(points.nonEmpty, "Color scale cannot be empty")
    type ScaleVal = (Temperature, Color)

    @tailrec
    def walk(scale: Seq[ScaleVal], prev: (Temperature, Color)): (ScaleVal, ScaleVal) = {
      if (scale.isEmpty) (prev, prev)
      else if (scale.head._1 < value) walk(scale.tail, scale.head)
      else (prev, scale.head)
    }

    def interpolate(x0: Temperature, y0: Int, x1: Temperature, y1: Int, x: Temperature): Int = {
      round(y0 + (x - x0) * (y1 - y0) / (x1 - x0)).toInt
    }

    val sorted = points.toIndexedSeq.sortBy {case (temperature, _) => temperature}
    if (value <= sorted.head._1) sorted.head._2
    else if (value >= sorted.last._1) sorted.last._2
    else {
      val ((x0, y0), (x1, y1)) = walk(sorted, (value, Color(0, 0, 0)))
      Color(
        interpolate(x0, y0.red, x1, y1.red, value),
        interpolate(x0, y0.green, x1, y1.green, value),
        interpolate(x0, y0.blue, x1, y1.blue, value)
      )
    }
  }

  /**
    * @param temperatures Known temperatures
    * @param colors Color scale
    * @return A 360×180 image where each pixel shows the predicted temperature at its location
    */
  def visualize(temperatures: Iterable[(Location, Temperature)], colors: Iterable[(Temperature, Color)]): Image = {
    ???
  }

}

