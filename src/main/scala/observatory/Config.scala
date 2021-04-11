package observatory

import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkContext
import org.apache.spark.sql.SparkSession

object Config {
  Logger.getLogger("org.apache.spark").setLevel(Level.WARN)

  val spark: SparkSession = SparkSession.builder().appName("Observatory").master("local").getOrCreate()
  val sc: SparkContext = spark.sparkContext
}
