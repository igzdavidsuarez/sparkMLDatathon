// this is used to implicitly convert an RDD to a DataFrame.

import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.classification.LogisticRegression
import org.apache.spark.ml.feature.{HashingTF, Tokenizer}
import org.apache.spark.sql.{SQLContext, Row}
import org.apache.spark.mllib.linalg.Vector

import scala.beans.BeanInfo

import org.apache.spark.{SparkConf, SparkContext}

@BeanInfo
case class LabeledPerson(id: Long, text: String, label: Double)

@BeanInfo
case class Person(id: Long, text: String)


object TestSparkSql {
  def main(args: Array[String]) {
    val conf = new SparkConf().setAppName("TestSparkSql")
    val sc = new SparkContext(conf)

    // sc is an existing SparkContext.
    val sqlContext = new SQLContext(sc)
    // this is used to implicitly convert an RDD to a DataFrame.
    import sqlContext.implicits._

    // Prepare training documents, which are labeled.
    val training = sc.parallelize(Seq(
      LabeledPerson(0L, "david suarez de triana", 1.0),
      LabeledPerson(1L, "jose manuel de la rinconada", 0.0),
      LabeledPerson(2L, "angel sanchez de parla", 0.0),
      LabeledPerson(3L, "antonio zurita de triana", 1.0)))

    // Configure an ML pipeline, which consists of three stages: tokenizer, hashingTF, and lr.
    val tokenizer = new Tokenizer()
      .setInputCol("text")
      .setOutputCol("words")
    val hashingTF = new HashingTF()
      .setNumFeatures(1000)
      .setInputCol(tokenizer.getOutputCol)
      .setOutputCol("features")
    val lr = new LogisticRegression()
      .setMaxIter(10)
      .setRegParam(0.001)
    val pipeline = new Pipeline()
      .setStages(Array(tokenizer, hashingTF, lr))

    // Fit the pipeline to training documents.
    val model = pipeline.fit(training.toDF())

    // Prepare test documents, which are unlabeled.
    val test = sc.parallelize(Seq(
      Person(4L, "nico nico de mostoles")))

    // Make predictions on test documents.
    model.transform(test.toDF())
      .select("id", "text", "probability", "prediction")
      .collect()
      .foreach { case Row(id: Long, text: String, prob: Vector, prediction: Double) =>
        println(s"($id, $text) --> prob=$prob, prediction=$prediction")
      }

    sc.stop()
  }
}
