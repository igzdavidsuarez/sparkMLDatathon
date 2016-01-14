// this is used to implicitly convert an RDD to a DataFrame.

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql._
import org.apache.spark.sql.hive.HiveContext

object TestSparkSql {
  def main(args: Array[String]) {
    val conf = new SparkConf().setAppName("TestSparkSql")
    val sc = new SparkContext(conf)

    // A hive context adds support for finding tables in the MetaStore and writing queries
    // using HiveQL. Users who do not have an existing Hive deployment can still create a
    // HiveContext. When not configured by the hive-site.xml, the context automatically
    // creates metastore_db and warehouse in the current directory.
    val hiveContext = new HiveContext(sc)
    import hiveContext.implicits._
    import hiveContext.sql

    // Queries are expressed in HiveQL
    println("Result of 'SELECT *': ")
    sql("use datathon")
      sql("SELECT * FROM mini_consumption").collect().foreach((u:Row) => println("THIS IS THE ROW", u))

    sc.stop()
  }
}
