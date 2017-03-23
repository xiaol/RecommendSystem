
name := """RecommendSystem"""

version := "0.0.1"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs
)

libraryDependencies +=  "org.apache.spark" % "spark-mllib_2.11" % "2.0.0"

libraryDependencies += "org.postgresql" % "postgresql" % "9.4-1204-jdbc41"

libraryDependencies += "org.apache.httpcomponents" % "httpclient" % "4.5.2"

dependencyOverrides += "com.fasterxml.jackson.core" % "jackson-databind" % "2.6.5"

dependencyOverrides += "com.fasterxml.jackson.core" % "jackson-core" % "2.6.5"

dependencyOverrides += "com.fasterxml.jackson.core" % "jackson-annotations" % "2.6.5"

dependencyOverrides += "org.apache.httpcomponents" % "httpcore" % "4.4.4"

//libraryDependencies += "com.jolbox" % "bonecp" % "0.8.0.RELEASE"

libraryDependencies += "com.zaxxer" % "HikariCP" % "2.5.1"

libraryDependencies += "redis.clients" % "jedis" % "2.9.0"