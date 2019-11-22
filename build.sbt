
name := "growinStartProject"

version := "0.1"

scalaVersion := "2.13.0"

lazy val global = project.in(file(".")).aggregate(core, akka, play, lagom)

lazy val core = project

lazy val akka = project
  .dependsOn(core)
  .settings(libraryDependencies ++= Seq(
    "com.typesafe.akka" %% "akka-http"   % "10.1.8",
    "com.typesafe.akka" %% "akka-stream" % "2.5.23",
    "com.typesafe.akka" %% "akka-http-spray-json" % "10.1.8"
  ))

lazy val play = project
  .dependsOn(core)
  .enablePlugins(PlayScala)
  .settings(libraryDependencies += guice)

val macwire = "com.softwaremill.macwire" %% "macros" % "2.2.5" % "provided"

lazy val lagom = project.dependsOn(core)
  .aggregate(`lagomgus-api`, `lagomgus-impl`)

lazy val `lagomgus-api` = (project in file("lagom/lagomgus-api"))
  .dependsOn(core)
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslApi
    )
  )

lazy val `lagomgus-impl` = (project in file("lagom/lagomgus-impl"))
  .enablePlugins(LagomScala)
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslPersistenceCassandra,
      lagomScaladslKafkaBroker,
      macwire
    )
  )
  .settings(lagomForkedTestSettings: _*)
  .dependsOn(core, `lagomgus-api`)

lazy val slick = project
  .dependsOn(core)
  .settings(libraryDependencies ++= Seq(
    "com.typesafe.slick" %% "slick" % "3.3.1",
    "org.slf4j" % "slf4j-nop" % "1.7.26",
    "com.typesafe.slick" %% "slick-hikaricp" % "3.3.1"
  ))

val Http4sVersion = "0.20.0"
val CirceVersion = "0.11.1"
val LogbackVersion = "1.2.3"
lazy val http4s = project
  .dependsOn(core)
  .settings(
    scalacOptions ++= Seq("-Ypartial-unification"),
    libraryDependencies ++= Seq(
      "org.http4s"      %% "http4s-blaze-server" % Http4sVersion,
      "org.http4s"      %% "http4s-blaze-client" % Http4sVersion,
      "org.http4s"      %% "http4s-circe"        % Http4sVersion,
      "org.http4s"      %% "http4s-dsl"          % Http4sVersion,
      "io.circe"        %% "circe-generic"       % CirceVersion,
      "ch.qos.logback"  %  "logback-classic"     % LogbackVersion
    ),
    addCompilerPlugin("org.spire-math" %% "kind-projector"     % "0.9.6"),
    addCompilerPlugin("com.olegpy"     %% "better-monadic-for" % "0.2.4")
  )

lazy val doobie = project
  .dependsOn(core)
  .settings(libraryDependencies ++= Seq(
    "org.tpolecat" %% "doobie-core"      % "0.7.0",
    "org.tpolecat" %% "doobie-h2"        % "0.7.0"
  ))