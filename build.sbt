name := "SparkML"

version := "1.0"

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "1.6.0" % Provided,
  "org.apache.spark" %% "spark-sql" % "1.6.0" % Provided,
  "org.apache.spark" %% "spark-hive" % "1.6.0" % Provided,
  "org.apache.spark" %% "spark-mllib" % "1.6.0" % Provided
)


resolvers ++= Seq(
  "clojars.org" at "http://clojars.org/repo"
)

scalacOptions += "-Yresolve-term-conflict:package"