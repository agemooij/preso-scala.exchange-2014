package preso

import akka.actor._
import akka.io.IO

import spray.can.Http

object Main extends App {
  implicit val system = ActorSystem("preso-advanced")

  val api = system.actorOf(Props[MainActor], "main-routing-actor")

  IO(Http)(system) ! Http.Bind(listener = api, interface = "0.0.0.0", port = Settings(system).Http.Port)
}
