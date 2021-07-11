package com.livesafe.tips

import scala.concurrent.{ ExecutionContext, Future }

import com.livesafe.tips.Repository.RecordNotFoundException

/**
 * Asynchronous in-memory repository. Meant to mock / imitate a true database connection
 * Subclass this with a concrete data type to use.
 * Example usage:
 * {{{
 *   case class Foo(a: Int, id: String)
 *   class FooRepository extends Repository[Foo](_.id)
 * }}}
 * @param extractId A function which, given an instance of [[A]], will extract an ID for A as a String
 * @tparam A The subject of this repository
 */
abstract class Repository[A <: AnyRef](extractId: (A) => String) {

  private var data: Map[String, A] = Map.empty

  private implicit val executionContext: ExecutionContext = scala.concurrent.ExecutionContext.Implicits.global

  def save(a: A): Future[String] = {
    Future {
      val id = extractId(a)
      data = data.updated(id, a)
      id
    }
  }

  def get(id: String): Future[A] = {
    Future {
      data.get(id) match {
        case Some(a) => a
        case None => throw RecordNotFoundException(s"No record with id ${id} found in repository")
      }
    }
  }

  def getAll(): Future[List[A]] = {
    Future {
      data.values.toList
    }
  }

}
object Repository {
  case class RecordNotFoundException(msg: String) extends Exception
}


