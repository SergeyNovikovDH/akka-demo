name := "akka-demo"

version := "0.0.1"

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-camel" % "2.3.12",
  "com.typesafe.akka" %% "akka-contrib" % "2.3.12",
  "org.apache.camel" % "camel-jetty" % "2.15.2",
  "org.apache.camel" % "camel-rabbitmq" % "2.15.2",
  "org.slf4j" % "slf4j-api" % "1.7.2",
  "ch.qos.logback" % "logback-classic" % "1.0.7"
)

fork in run := true
