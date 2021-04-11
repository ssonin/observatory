package observatory

import java.sql.Date
import java.time.LocalDate

import observatory.Config.{sc, spark}
import org.apache.spark.sql.functions.{avg, lit}
import org.apache.spark.sql.types._
import org.apache.spark.sql.{Column, DataFrame, Row}

import scala.io.Source

/**
  * 1st milestone: data extraction
  */
object Extraction extends ExtractionInterface {

  import spark.implicits._

  private val stationsSchema: StructType = StructType(Array(
    StructField("stn", StringType),
    StructField("wban", StringType),
    StructField("lat", DoubleType),
    StructField("lon", DoubleType)))

  private val temperaturesSchema: StructType = StructType(Array(
    StructField("stn", StringType),
    StructField("wban", StringType),
    StructField("month", IntegerType),
    StructField("day", IntegerType),
    StructField("temperature", DoubleType)))

  /**
    * @param year             Year number
    * @param stationsFile     Path of the stations resource file to use (e.g. "/stations.csv")
    * @param temperaturesFile Path of the temperatures resource file to use (e.g. "/1975.csv")
    * @return A sequence containing triplets (date, location, temperature)
    */
  def locateTemperatures(year: Year, stationsFile: String, temperaturesFile: String): Iterable[(LocalDate, Location, Temperature)] = {
    sparkLocateTemperatures(year, stationsFile, temperaturesFile)
      .map {
        case Row(lat: Double, lon: Double, year: Int, month: Int, day: Int, temperature: Double) =>
          (Date.valueOf(LocalDate.of(year, month, day)), Location(lat, lon), temperature)
      }.collect()
      .map {
        case (date: Date, location: Location, temperature: Double) =>
          (date.toLocalDate, location, temperature)
      }
  }

  def sparkLocateTemperatures(year: Year, stationsFile: String, temperaturesFile: String): DataFrame = {
    val stationsDF = read(stationsFile, stationsSchema)
    val temperaturesDF = read(temperaturesFile, temperaturesSchema)

    stationsDF.filter($"lat".isNotNull && $"lon".isNotNull)
      .join(temperaturesDF, stationsDF("stn") <=> temperaturesDF("stn")
        && stationsDF("wban") <=> temperaturesDF("wban"))
      .withColumn("year", lit(year))
      .select($"lat", $"lon", $"year", $"month", $"day", celsius($"temperature").as("temperature"))
  }

  /**
    * @param records A sequence containing triplets (date, location, temperature)
    * @return A sequence containing, for each location, the average temperature over the year.
    */
  def locationYearlyAverageRecords(records: Iterable[(LocalDate, Location, Temperature)]): Iterable[(Location, Temperature)] = {
    val df = records.map {
      case (localDate, location, temperature) => (Date.valueOf(localDate), location.lat, location.lon, temperature)
    }.toSeq.toDF("date", "lat", "lon", "temperature")
    locationYearlyAverageRecords(df)
      .map {
        case Row(lat: Double, lon: Double, temperature: Double) => (Location(lat, lon), temperature)
      }.collect()
  }

  def locationYearlyAverageRecords(records: DataFrame): DataFrame = {
    records.groupBy($"lat", $"lon")
      .agg(avg($"temperature").as("temperature"))
  }

  private def read(path: String, schema: StructType): DataFrame = {
    val lines = Source.fromInputStream(getClass.getResourceAsStream(path), "utf-8").getLines
    spark.read.schema(schema).csv(sc.parallelize(lines.toSeq).toDS)
  }

  private def celsius(fahrenheit: Column): Column =
    (fahrenheit - 32) * 5 / 9
}
