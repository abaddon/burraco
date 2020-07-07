import scoverage.ScoverageKeys._

name := "burraco"

version := "0.1"

organization := "com.abaddon83"

scalaVersion := "2.13.2"

coverageExcludedPackages := "<empty>;com.abaddon83.libs.*;(.)*[F-f]ake(.)*"
coverageMinimum := 80
coverageFailOnMinimum := false

libraryDependencies ++= {
  val akkaVersion = "2.6.6"
  val akkaHttpVersion = "10.1.12"
  Seq(
    "net.codingwell" %% "scala-guice" % "4.2.7",
    // akka streams
    "com.typesafe.akka" %% "akka-stream" % akkaVersion,
    // akka actor
    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    // akka http
    "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
    "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
    // log
    "com.typesafe.akka" %% "akka-slf4j"      % akkaVersion,
    "ch.qos.logback"    %  "logback-classic" % "1.2.3" % "runtime",


    //"com.typesafe.akka" %% "akka-http-xml" % akkaHttp,

  //"com.typesafe.akka" %% "akka-actor" % akkaVersion,
    //"com.typesafe.akka" %% "akka-http"       % akkaHttp,

    //"com.typesafe.akka" %% "akka-http-spray-json" % akkaHttp,
    //"com.typesafe.akka" %% "akka-http-xml" % akkaHttp,
    //"com.typesafe.akka" %% "akka-stream" % akkaVersion,
    //"com.typesafe.akka" %% "akka-http-testkit" % akkaHttp % Test,
    //"com.typesafe.akka" %% "akka-testkit" % akkaVersion % Test,
    //"com.typesafe.akka" %% "akka-stream-testkit" % akkaVersion % Test,
    "org.scalatest" %% "scalatest" % "3.1.1" % Test
  )

}
