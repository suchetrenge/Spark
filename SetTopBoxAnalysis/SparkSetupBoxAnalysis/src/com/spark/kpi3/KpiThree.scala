package com.spark.kpi3

import scala.math.random

import org.apache.spark.sql.SparkSession
import scala.xml.XML

object KpiThree {
  def main(args: Array[String]) {
    System.setProperty("hadoop.home.dir", "D:\\Study\\DataFlair\\software\\hadoop\\hadoop-3.1.2")
    System.setProperty("spark.sql.warehouse.dir", "D:\\Study\\DataFlair\\software\\spark\\spark-2.0.2-bin-hadoop2.6\\spark-warehouse")
    //    if (args.length < 2) {
    //      System.err.println("Usage: JavaWordCount <Input-File> <Output-File>");
    //      System.exit(1);
    //    }

    val spark = SparkSession
      .builder
      .appName("Wordcount")
      .master("local")
      .getOrCreate()

    val data = spark.read.textFile("D:\\Study\\DataFlair\\Assignement_&_Projects\\projects_spark\\SetTopBox_Project\\data\\Set_Top_Box_Data.txt").rdd

    val result = data.filter(line => (line.split("\\^")(2).toString() == "102" || line.split("\\^")(2).toString() == "103"))
      .map {
        line =>
          {
            val splitData = line.split("\\^")
            val xml = XML.loadString(splitData(4))
            val childList = xml.child
            var offerId = ""
            var price = ""
            for (element <- childList) {
              val xmlChild = XML.loadString(element.toString())
              if (xmlChild.attribute("n") != None) {
                if (xmlChild.attribute("n").get.toString() == "OfferId") {
                  offerId = xmlChild.attribute("v").get.toString()
                }
                if (xmlChild.attribute("n").get.toString() == "Price") {
                  price = xmlChild.attribute("v").get.toString()
                }
              }
            }
            (offerId, price.toFloat)
          }
      }
      .reduceByKey(Math.max(_, _))

    result.foreach(println)

    spark.stop
  }

}