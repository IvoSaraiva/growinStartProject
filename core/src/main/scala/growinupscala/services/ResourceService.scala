package growinupscala.services

import growinupscala.model.Resource
import growinupscala.{Id, JSON}

import scala.concurrent.Future

/**
 * Mock implementation of [[Service]].
 */
case class ResourceService(resource: String) extends Service[Resource] {
  override def create(resource: JSON): Future[Id] = Future.successful("SomeId")

  override def read(id: Id): Future[Resource] = Future.successful(new Resource {
    override val name: String = resource
  })

  override def update(id: Id, resource: JSON): Future[Unit] = Future.successful(())

  override def delete(id: Id): Future[Unit] = Future.successful(())
}
