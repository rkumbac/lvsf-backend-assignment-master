package com.livesafe.tips

import org.scalatest._
import org.scalatest.concurrent.ScalaFutures

class RepositorySpec extends FreeSpec with Matchers with ScalaFutures {
  case class Foo(id: String, count: Int)
  class FooRepository extends Repository[Foo](_.id)

  def withFixture[A](block: (FooRepository => A)): A = {
    val repository = new FooRepository()
    block(repository)
  }

  "Repository should" - {
    "store records" in {
      withFixture { repo =>
        val exemplar = Foo("an-id", 42)
        val saveResult = repo.save(exemplar).futureValue
        val getResult = repo.get(saveResult).futureValue

        info("The ID is returned on save")
        saveResult shouldBe exemplar.id

        info("The fetched record matches the original")
        getResult shouldBe exemplar
      }
    }
    "update records with same id" in {
      withFixture { repo =>
        val exemplar = Foo("an-id", 42)
        val saveResult = repo.save(exemplar).futureValue
        val updateResult = repo.save(exemplar.copy(count = 100))
        val getResult = repo.get(exemplar.id).futureValue
        getResult.count shouldBe 100
      }
    }
    "respond with a failed future when ID is not found" in {
      withFixture { repo =>
        repo.get("an-id").failed.futureValue shouldBe a[Repository.RecordNotFoundException]
      }
    }
  }
}