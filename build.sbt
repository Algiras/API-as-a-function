name := "API-as-a-function"

resolvers += Resolver.sonatypeRepo("releases")

version := "0.1"

scalacOptions ++= Seq(
"-feature",
"-deprecation",
"-unchecked",
"-language:postfixOps",
"-language:higherKinds",
"-Ypartial-unification"
)

scalaVersion := "2.12.8"

addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.8")

libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-core" % "1.5.0",
  "org.typelevel" %% "cats-effect" % "1.0.0",
  "io.monix" %% "monix" % "3.0.0-RC2",
  "org.specs2" %% "specs2-core" % "4.3.4" % "test")

scalacOptions in Test ++= Seq("-Yrangepos")