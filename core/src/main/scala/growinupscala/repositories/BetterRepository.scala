package growinupscala.repositories

import growinupscala.Id

import scala.language.higherKinds

trait BetterRepository[F[_], R] {
  def create(resource: R): F[Id]
  def read(id: Id): F[R]
  def update(id: Id, resource: R): F[Unit]
  def delete(id: Id): F[Unit]
}
