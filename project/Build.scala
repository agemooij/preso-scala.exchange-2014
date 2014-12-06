import sbt._
import sbt.Keys._

import com.scalapenos.sbt.prompt._
import SbtPrompt._
import autoImport._

import spray.revolver.RevolverPlugin.Revolver

object Build extends Build {
  import Dependencies._
  import Formatting._

  lazy val basicSettings = Seq(
    organization := "com.scalapenos",
    version := "0.1.0",
    scalaVersion := "2.11.4",
    scalacOptions := basicScalacOptions,
    incOptions := incOptions.value.withNameHashing(true),
    promptTheme := PromptThemes.Scalapenos
  )

  lazy val libSettings = basicSettings ++ dependencySettings ++ formattingSettings
  lazy val appSettings = libSettings ++ Revolver.settings

  lazy val preso = Project("preso", file("."))
    .settings(appSettings: _*)
    .settings(mainClass := Some("preso.Main"))
    .settings(libraryDependencies ++=
      compile(
        akkaActor,
        akkaSlf4j,
        sprayCan,
        sprayRouting,
        sprayJson,
        ficus,
        logback
      ) ++
      test(
        akkaTestkit,
        sprayTestkit,
        scalatest
      )
    )

  val basicScalacOptions = Seq(
    "-encoding", "utf8",
    "-target:jvm-1.7",
    "-feature",
    "-language:implicitConversions",
    "-language:postfixOps",
    "-unchecked",
    "-deprecation",
    "-Xlog-reflective-calls"
  )

  val fussyScalacOptions = basicScalacOptions ++ Seq(
    "-Ywarn-unused",
    "-Ywarn-unused-import"
  )
}
