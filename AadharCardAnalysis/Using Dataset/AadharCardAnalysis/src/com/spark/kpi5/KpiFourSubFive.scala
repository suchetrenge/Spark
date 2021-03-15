package com.spark.kpi5

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.expressions.Window
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types._

object KpiFourSubFive {
  case class aadharCase(
    _c0:  String,
    _c1:  String,
    _c2:  String,
    _c3:  String,
    _c4:  String,
    _c5:  String,
    _c6:  String,
    _c7:  String,
    _c8:  String,
    _c9:  String,
    _c10: String,
    _c11: String,
    _c12: String);

  def main(args: Array[String]) {
    System.setProperty("hadoop.home.dir", "D:\\Study\\DataFlair\\software\\hadoop\\hadoop-3.1.2")
    System.setProperty("spark.sql.warehouse.dir", "file:///D:/Study/DataFlair/software/spark/spark-2.0.2-bin-hadoop2.6/spark-warehouse")

    //    if (args.length < 2) {
    //      System.err.println("Usage: JavaWordCount <Input-File> <Output-File>");
    //      System.exit(1);
    //    }

    val spark = SparkSession.builder().appName("Spark SQL").master("local").getOrCreate()

    import spark.implicits._
    val path = "D:\\Study\\DataFlair\\Assignement_&_Projects\\projects_spark\\Aadhar_Card_Analysis\\data\\data\\aadhaar\\aadhaardata.csv"
    val data = spark.read.csv(path).as[aadharCase]

    // Total count of aadhar generated for Females
    val count = data.agg(sum("_c9")).first().get(0)

    val interval = 10
    val result1 = data.withColumn("range", $"_c8" - ($"_c8" % interval))
      .withColumn("range", concat($"range", lit(" - "), $"range" + interval))
      .groupBy($"range")
      .agg((sum("_c9") * 100.0 / count).alias("most_aadhar_generated"))
      .orderBy(desc("most_aadhar_generated"))
      .show(10)

    spark.stop
  }
}