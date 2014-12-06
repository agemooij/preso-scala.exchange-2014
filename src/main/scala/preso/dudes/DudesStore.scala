package preso
package dudes

import scala.concurrent._

trait DudeStoreProvider {
  val dudeStore: DudeStore

  trait DudeStore {
    def allDudes: Future[Set[Dude]]
    def findDudeById(id: Long): Future[Option[Dude]]
    def createDude(dude: Dude): Future[Long]
    def updateDude(id: Long, dude: Dude): Future[Option[Dude]]
    def deleteDude(id: Long): Future[Option[Dude]]
  }
}

import duration._
import akka.actor._
import akka.pattern._
import akka.util._

import preso.util._

/**
 * Rough actor-based implementation of an in-memory Dude store.
 * Not really relevant to the topic at hand but it's nice to have
 * a proper, threadsafe implementation ;)
 */
trait InMemoryDudeStoreProvider extends DudeStoreProvider with ActorCreationSupport {
  val dudeStore: DudeStore = new InMemoryDudeStore()

  private class InMemoryDudeStore extends DudeStore {
    import InMemoryDudeStoreActorMessages._

    implicit val askTimeout = Timeout(2 seconds)
    private val productStoreActor = getOrCreateActor(Props[InMemoryDudeStoreActor], "in-memory-dude-store-actor")

    def allDudes = productStoreActor.ask(GetAllDudes).mapTo[Set[Dude]]
    def findDudeById(id: Long) = productStoreActor.ask(FindDudeById(id)).mapTo[Option[Dude]]
    def deleteDude(id: Long) = productStoreActor.ask(DeleteDude(id)).mapTo[Option[Dude]]
    def createDude(dude: Dude) = productStoreActor.ask(CreateDude(dude)).mapTo[Long]
    def updateDude(id: Long, dude: Dude) = productStoreActor.ask(UpdateDude(id, dude)).mapTo[Option[Dude]]
  }
}

object InMemoryDudeStoreActorMessages {
  case object GetAllDudes
  case class FindDudeById(id: Long)
  case class DeleteDude(id: Long)
  case class CreateDude(dude: Dude)
  case class UpdateDude(id: Long, dude: Dude)
}

class InMemoryDudeStoreActor extends Actor with ActorLogging {
  private val theDude = Dude("agemooij", "Age Mooij", Set("Your humble presenter"))
  private var dudes = Map(0L -> theDude)
  private var nextId = 1L

  import InMemoryDudeStoreActorMessages._

  def receive = {
    case GetAllDudes          ⇒ sender() ! dudes
    case FindDudeById(id)     ⇒ sender ! dudes.get(id)
    case DeleteDude(id)       ⇒ sender ! deleteDude(id)
    case CreateDude(dude)     ⇒ sender ! createDude(dude)
    case UpdateDude(id, dude) ⇒ sender ! updateDude(id, dude)
  }

  private def deleteDude(id: Long): Option[Dude] = {
    val (deleted, leftovers) = dudes.partition { case (key, _) ⇒ key == id }
    dudes = leftovers
    deleted.values.headOption
  }

  private def createDude(dude: Dude): Long = {
    val id = nextId
    nextId += 1L
    updateDude(id, dude)
    id
  }

  private def updateDude(id: Long, dude: Dude): Option[Dude] = {
    dudes = dudes + (id -> dude)
    Some(dude)
  }
}
