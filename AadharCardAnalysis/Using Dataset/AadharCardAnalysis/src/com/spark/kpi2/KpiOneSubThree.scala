package com.spark.kpi2

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.expressions.Window
import org.apache.spark.sql.functions.{count,desc,when,col,min}

object KpiOneSubThree {
    
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
    
    
    val maleCount = data.groupBy("_c3").agg(count(when(col("_c7").contains("M"), 1)).alias("Male"),
        count(when(col("_c7").contains("F"),1)).alias("Female")).show(38)

    spark.stop
  }
}