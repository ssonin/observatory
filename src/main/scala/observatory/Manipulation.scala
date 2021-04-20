package observatory

import observatory.Visualization.predictTemperature

import scala.collection.concurrent.TrieMap

/**
  * 4th milestone: value-added information
  */
object Manipulation extends ManipulationInterface {

  private val cache = new TrieMap[(Iterable[(Location, Temperature)], GridLocation), Temperature]()

  /**
    * @param temperatures Known temperatures
    * @return A function that, given a latitude in [-89, 90] and a longitude in [-180, 179],
    *         returns the predicted temperature at this location
    */
  def makeGrid(temperatures: Iterable[(Location, Temperature)]): GridLocation => Temperature = {
    gridLoc => cache.getOrElseUpdate((temperatures, gridLoc), predictTemperature(temperatures, gridLoc.location))
  }

  /**
    * @param temperaturess Sequence of known temperatures over the years (each element of the collection
    *                      is a collection of pairs of location and temperature)
    * @return A function that, given a latitude and a longitude, returns the average temperature at this location
    */
  def average(temperaturess: Iterable[Iterable[(Location, Temperature)]]): GridLocation => Temperature = {
    gridLoc => {
      val (sumOfTemperatures, numberOfMeasurements) = temperaturess
        .map(temp => makeGrid(temp)(gridLoc))
        .aggregate(0.0, 0)(
          {case ((sum, num), temp) => (sum + temp, num + 1)},
          {case ((sum1, num1), (sum2, num2)) => (sum1 + sum2, num1 + num2)})
      sumOfTemperatures / numberOfMeasurements
    }
  }

  /**
    * @param temperatures Known temperatures
    * @param normals A grid containing the “normal” temperatures
    * @return A grid containing the deviations compared to the normal temperatures
    */
  def deviation(temperatures: Iterable[(Location, Temperature)],
                normals: GridLocation => Temperature): GridLocation => Temperature = {
    gridLoc => {
      makeGrid(temperatures)(gridLoc) - normals(gridLoc)
    }
  }


}

