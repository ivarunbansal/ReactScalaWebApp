lazy val akkaHttpVersion = "10.1.8"
lazy val akkaVersion    = "2.5.22"

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization    := "com.shoppingCart",
      scalaVersion    := "2.12.7"
    )),
    name := "server",
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-http"            % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-http-xml"        % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-stream"          % akkaVersion,
      "com.typesafe.akka" %% "akka-http-testkit"    % akkaHttpVersion % Test,
      "com.typesafe.akka" %% "akka-testkit"         % akkaVersion     % Test,
      "com.typesafe.akka" %% "akka-stream-testkit"  % akkaVersion     % Test,
      "org.mongodb.scala" %% "mongo-scala-driver" % "2.6.0",
      "org.scalatest"     %% "scalatest"            % "3.0.5"         % Test,
      "org.xerial.snappy" % "snappy-java" % "1.1.7.3"
    )
  )
