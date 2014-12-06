package preso

import akka.actor._
import net.ceedubs.ficus.Ficus._

import util._

object Settings extends ExtensionKey[Settings]

class Settings(system: ExtendedActorSystem) extends Extension {
  private val config = system.settings.config

  object Http {
    val Port = config.as[Int]("preso.http.port")
    val EnforceHttps = config.as[Boolean]("preso.http.enforce-https")
  }
}

trait SettingsProvider extends ActorRefFactoryProvider {
  lazy val settings: Settings = Settings(actorSystem)
}
