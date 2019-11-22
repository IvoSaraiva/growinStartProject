package growinupscala.services

import growinupscala.{Id, JSON}

import scala.concurrent.Future

trait Service[R] {
  def create(resource: JSON): Future[Id]
  def read(id: Id): Future[R]
  def update(id: Id, resource: JSON): Future[Unit]
  def delete(id: Id): Future[Unit]
}
