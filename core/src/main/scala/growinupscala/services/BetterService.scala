package growinupscala.services

import growinupscala.{Id, JSON}

import scala.language.higherKinds

/**
 * This trait is to be used instead of [[Service]] when working with Http4s.
 */
trait BetterService[F[_], R] {
  def create(resource: JSON): F[Id]
  def read(id: Id): F[R]
  def update(id: Id, resource: JSON): F[Unit]
  def delete(id: Id): F[Unit]
}
