package com.akshitbhatia.spark
import org.apache.spark.sql.functions._
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.execution.streaming.MemoryStream

object streaming_app {
  case class Raw(Name: String, Age: Int)

  def main(args: Array[String]): Unit = {
    val spark: SparkSession = SparkSession
      .builder()
      .appName("Streaming-app")
      .master("local")
      .getOrCreate()

    import spark.implicits._   

    val rawIn= MemoryStream[Raw](1,spark.sqlContext )

    rawIn.addData(Raw("akshit",25))
    rawIn.addData(Raw("nikhil",24))
    rawIn.addData(Raw("aman", 25))
    rawIn.addData(Raw("tarun", 24))
    rawIn.addData(Raw("null", 24))
    rawIn.addData(Raw("Nimish",102))
    rawIn.addData(Raw("Akash",1020))
    rawIn.addData(Raw("null", 2124))
    rawIn.addData(Raw("null", 2424))
    rawIn.addData(Raw("yashit",25))
    rawIn.addData(Raw("Ankit",24))
    rawIn.addData(Raw("Vikesh", 25))
    rawIn.addData(Raw("push", 24))
    rawIn.addData(Raw("Mahi", 24))
    rawIn.addData(Raw("null", 24))

    /**
    
     Converting rawIn into DF

     **/


    val inDF= rawIn.toDF()


    val age_greater= inDF
      .filter($"Age" >= 99)
      .groupBy($"Name",$"Age")
      .agg(avg($"Age").alias("Avg-Age"),min($"Age").alias("Min-Age"),max($"Age").alias("Max-Age"))
      .orderBy($"Age")
      .select($"Name",$"Age")


    val null_data= inDF
      .filter($"Name" === "null")
      .groupBy($"Name",$"Age")
      .agg(avg($"Age").alias("Avg-Age"),min($"Age").alias("Min-Age"),max($"Age").alias("Max-Age"))
      .orderBy($"Age")
      .select($"Name",$"Age")

    val filter_data= inDF
      .filter($"Age"  <= 99)
      .filter($"Name" =!= "null")
      .groupBy($"Name",$"Age")
      .agg(avg($"Age").alias("Avg-Age"),min($"Age").alias("Min-Age"),max($"Age").alias("Max-Age"))
      .orderBy($"Age")
      .select($"Name",$"Age")




    val age_filter = age_greater
      .writeStream
      .outputMode("complete")
      .format("memory")
      .queryName("age_greater_than_99")
      .start()


    val null_filter = null_data
      .writeStream
      .outputMode("complete")
      .format("memory")
      .queryName("all_null_data")
      .start()


    val filter_data_n = filter_data
      .writeStream
      .outputMode("complete")
      .format("memory")
      .queryName("all_filter_data")
      .start()



    val age_filter_n = spark.table("age_greater_than_99").as[(String,Int)]
    age_filter.processAllAvailable()
    age_filter_n.show
    age_filter_n.write.format("parquet").mode("append").save("/home/akshit/Desktop/project/age_greater_than_99_parquet_data")


    val null_filter_n = spark.table("all_null_data").as[(String,Int)]
    null_filter.processAllAvailable()
    null_filter_n.show
    null_filter_n.write.format("parquet").mode("append").save("/home/akshit/Desktop/project/all_null_data_parquet")


    val filter_data_tbl = spark.table("all_filter_data").as[(String,Int)]
    filter_data_n.processAllAvailable()
    filter_data_tbl.show
    filter_data_tbl.write.format("parquet").mode("append").save("/home/akshit/Desktop/project/all_filter_data_parquet")

  }
}

