package preso.dudes

import spray.json._
import DefaultJsonProtocol._

case class Dude(id: String, name: String, tags: Set[String] = Set.empty)

object Dude {
  implicit val json = jsonFormat3(Dude.apply)
}
