package observatory

import observatory.Config.{sc, spark}
import com.sksamuel.scrimage.{Image, Pixel}
import org.apache.spark.sql.{Column, DataFrame}
import org.apache.spark.sql.functions._

import scala.math.Pi

/**
  * 2nd milestone: basic visualization
  */
object Visualization extends VisualizationInterface {

  import spark.implicits._

  private val iwdPower = 2
  private val earthRadius = 6371
  private val distancePrecision = 1

  /**
    * @param temperatures Known temperatures: pairs containing a location and the temperature at this location
    * @param location Location where to predict the temperature
    * @return The predicted temperature at `location`
    */
  def predictTemperature(temperatures: Iterable[(Location, Temperature)], location: Location): Temperature = {
    val df = temperatures.map {case (loc, temp) => (loc.lat, loc.lon, temp)}
      .toSeq.toDF("lat_i", "lon_i", "temp")
      .withColumn("lat", lit(location.lat))
      .withColumn("lon", lit(location.lon))
      .withColumn("distance", distance($"lat_i", $"lon_i", $"lat", $"lon"))

    val close = df.where($"distance" === 0)
    if (close.isEmpty) sparkPredict(df) else df.first().getAs[Temperature]("temperature")
  }

//  def sparkPredictTemperature(temperatures: Iterable[(Location, Temperature)], location: Location): Temperature = {
//    val df = temperatures.map {case (loc, temp) => (loc.lat, loc.lon, temp)}
//      .toSeq.toDF("lat_i", "lon_i", "temp")
//      .withColumn("lat", lit(location.lat))
//      .withColumn("lon", lit(location.lon))
//      .withColumn("distance", distance($"lat_i", $"lon_i", $"lat", $"lon"))
//
//    val close = df.where($"distance" === 0)
//    if (close.isEmpty) sparkPredict(df) else df.first().getAs[Temperature]("temperature")
//  }

  def sparkPredict(temperatures: DataFrame): Temperature = {
    val augmented = temperatures.withColumn("denomTerm", lit(1) / pow(temperatures("distance"), 2))
    val nomTerm: Column = (augmented("temp") * augmented("denomTerm")).as("nomTerm")
    val r = augmented
      .select(nomTerm, $"denomTerm")
      .agg(sum($"nomTerm").as("nom"), sum($"denomTerm").as("denom"))
      .select($"nom", $"denom")
      .first()

    r.getAs[Double]("nom") / r.getAs[Double]("denom")
  }

  def distance(lat1: Column, lon1: Column, lat2: Column, lon2: Column): Column = {
    val calculated = when(lat1 === lat2 && lon1 === lon2, 0)
      .when(lat1 === negate(lat2) && (lon1 === (lon2 + Pi) || lon1 === (lon2 - Pi)), Pi)
      .otherwise {
        val (lat1r, lon1r, lat2r, lon2r) = (radians(lat1), radians(lon1), radians(lat2), radians(lon2))
        acos(sin(lat1r) * sin(lat2r) + cos(lat1r) * cos(lat2r) * cos(abs(lon1r - lon2r)))
      } * earthRadius
    when(calculated <= distancePrecision, 0).otherwise(calculated)
  }

  /**
    * @param points Pairs containing a value and its associated color
    * @param value The value to interpolate
    * @return The color that corresponds to `value`, according to the color scale defined by `points`
    */
  def interpolateColor(points: Iterable[(Temperature, Color)], value: Temperature): Color = {
    ???
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

