package growinupscala.services

import java.time.Instant

import growinupscala.model.DBResource
import growinupscala.repositories.Repository
import growinupscala.{Id, JSON}
import DBResourceService._
import scala.concurrent.Future

case class DBResourceService(rep: Repository[DBResource]) extends Service[DBResource] {
  override def create(resource: JSON): Future[Id] = rep.create(parseJson(resource))

  override def read(id: Id): Future[DBResource] = rep.read(id)

  override def update(id: Id, resource: JSON): Future[Unit] = rep.update(id, parseJson(resource))

  override def delete(id: Id): Future[Unit] = rep.delete(id)
}


object DBResourceService {
  def parseJson(resource: JSON): DBResource = DBResource("", None, 1L, Instant.now)
}