package preso.util

import scala.concurrent.ExecutionContext
import akka.actor._

trait ExecutionContextProvider {
  implicit def executionContext: ExecutionContext
}

trait ActorRefFactoryExecutionContextProvider extends ExecutionContextProvider with ActorRefFactoryProvider {
  implicit def executionContext = actorRefFactory.dispatcher
}

trait ActorExecutionContextProvider extends ExecutionContextProvider {
  this: Actor ⇒
  implicit def executionContext = context.dispatcher
}

