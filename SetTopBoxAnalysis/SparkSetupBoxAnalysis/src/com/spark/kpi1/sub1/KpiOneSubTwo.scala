package com.spark.kpi1.sub1

import scala.math.random

import org.apache.spark.sql.SparkSession
import scala.xml.XML

object KpiOneSubTwo {
  def main(args: Array[String]) {
    System.setProperty("hadoop.home.dir", "D:\\Study\\DataFlair\\software\\hadoop\\hadoop-3.1.2")
    System.setProperty("spark.sql.warehouse.dir", "D:\\Study\\DataFlair\\software\\spark\\spark-2.0.2-bin-hadoop2.6\\spark-warehouse")
    //    if (args.length < 2) {
    //      System.err.println("Usage: JavaWordCount <Input-File> <Output-File>");
    //      System.exit(1);
    //    }

    val spark = SparkSession
      .builder
      .appName("SetupBox")
      .master("local")
      .getOrCreate()

    val data = spark.read.textFile("D:\\Study\\DataFlair\\Assignement_&_Projects\\projects_spark\\SetTopBox_Project\\data\\Set_Top_Box_Data.txt").rdd

    val result = data.filter(line => line.split("\\^")(2).toString() == "100")
      .map {
        line =>
          {
            val splitData = line.split("\\^")
            val xml = XML.loadString(splitData(4))
            val childList = xml.child
            var durationValue = ""
            var channelNumber = ""
            for (element <- childList) {
              val xmlChild = XML.loadString(element.toString())
              if (xmlChild.attribute("n") != None) {
                if (xmlChild.attribute("n").get.toString() == "Duration") {
                  durationValue = xmlChild.attribute("v").get.toString()
                }
              }
              if (xmlChild.attribute("n") != None) {
                if (xmlChild.attribute("n").get.toString() == "ChannelNumber") {
                  channelNumber = xmlChild.attribute("v").get.toString()
                }
              }

            }
            (durationValue.toLong, channelNumber)
          }
      }
      .sortByKey(false)

    result.take(5).foreach(println)

    spark.stop
  }
}