package growinupscala.services

import cats.Monad
import growinupscala.{Id, JSON}
import growinupscala.model.Resource

import scala.language.higherKinds

case class BetterResourceService[F[_]](resource: Resource)(implicit F: Monad[F]) extends BetterService[F, Resource] {
  def create(resource: JSON): F[Id] = F.pure("SomeId")

  def read(id: Id): F[Resource] = F.pure(resource)

  def update(id: Id, resource: JSON): F[Unit] = F.unit

  def delete(id: Id): F[Unit] = F.unit
}