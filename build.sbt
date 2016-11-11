name := "digital-wallet"

version := "1.0"

scalaVersion := "2.11.8"

// set the main class for 'sbt run'
mainClass in (Compile, run) := Some("digitalwallet.DigitalWallet")

assemblyJarName in assembly := "projectAssembly.jar"
target in assembly := file("src/")

libraryDependencies ++= Seq(
  "org.scalatest" % "scalatest_2.11" % "2.2.4" % "test",
  "org.scalamock" %% "scalamock-scalatest-support" % "3.2" % "test",
  "org.mockito" % "mockito-all" % "1.8.4",
  "joda-time" % "joda-time" % "2.9",
  "org.joda" % "joda-convert" % "1.8"
)