package growinupscala.repositories

import growinupscala.{Id, JSON}

import scala.concurrent.Future

trait Repository[R] {
  def create(resource: R): Future[Id]
  def read(id: Id): Future[R]
  def update(id: Id, resource: R): Future[Unit]
  def delete(id: Id): Future[Unit]
}
