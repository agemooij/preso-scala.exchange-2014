package preso.util

import akka.actor._

trait ActorRefFactoryProvider {
  implicit def actorRefFactory: ActorRefFactory

  def actorSystem(implicit refFactory: ActorRefFactory): ExtendedActorSystem = spray.util.actorSystem(refFactory)
}

trait ActorRefFactoryProviderForActors extends ActorRefFactoryProvider {
  this: Actor ⇒
  def actorRefFactory = context
}
