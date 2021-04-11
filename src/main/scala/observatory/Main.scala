package observatory

object Main extends App {

  val list = Extraction.locateTemperatures(2015, "/stations.csv", "/2015.csv").take(100)
  val avg = Extraction.locationYearlyAverageRecords(list)
  println(avg)

}
