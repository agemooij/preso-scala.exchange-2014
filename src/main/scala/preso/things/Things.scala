package preso.things

import spray.json._
import DefaultJsonProtocol._

case class Thing(name: String)

object Thing {
  implicit val json = jsonFormat1(Thing.apply)
}
