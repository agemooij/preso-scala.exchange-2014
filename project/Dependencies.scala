import sbt._
import sbt.Keys._

object Dependencies {

  lazy val dependencySettings = Seq(
    resolvers := Seq("Spray Repository" at "http://repo.spray.io/")
  )

  val akkaVersion  = "2.3.7"
  val sprayVersion = "1.3.2"

  val akkaActor     = "com.typesafe.akka"  %% "akka-actor"       % akkaVersion
  val akkaSlf4j     = "com.typesafe.akka"  %% "akka-slf4j"       % akkaVersion

  val sprayCan      = "io.spray"           %% "spray-can"        % sprayVersion
  val sprayClient   = "io.spray"           %% "spray-client"     % sprayVersion
  val sprayRouting  = "io.spray"           %% "spray-routing"    % sprayVersion
  val sprayUtil     = "io.spray"           %% "spray-util"       % sprayVersion
  val sprayJson     = "io.spray"           %% "spray-json"       % "1.3.1"

  val ficus         = "net.ceedubs"        %% "ficus"            % "1.1.1"
  val logback       = "ch.qos.logback"     %  "logback-classic"  % "1.1.2"

  val akkaTestkit   = "com.typesafe.akka"  %% "akka-testkit"     % akkaVersion
  val sprayTestkit  = "io.spray"           %% "spray-testkit"    % sprayVersion
  val scalatest     = "org.scalatest"      %% "scalatest"        % "2.2.2"

  def compile   (deps: ModuleID*): Seq[ModuleID] = deps map (_ % "compile")
  def provided  (deps: ModuleID*): Seq[ModuleID] = deps map (_ % "provided")
  def test      (deps: ModuleID*): Seq[ModuleID] = deps map (_ % "test")
  def runtime   (deps: ModuleID*): Seq[ModuleID] = deps map (_ % "runtime")
  def container (deps: ModuleID*): Seq[ModuleID] = deps map (_ % "container")
}
