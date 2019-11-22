package growinupscala.repositories

import growinupscala.Id
import growinupscala.mappings.ResourceTable
import growinupscala.model.DBResource
import slick.jdbc.H2Profile.api._

import scala.concurrent.{ExecutionContext, Future}

class DBResourceRepository(db: Database)(implicit ec: ExecutionContext) extends Repository[DBResource] {

  override def read(id: Id): Future[DBResource] = {
    val query = ResourceTable.byId(id)
    val action = query.result.head
    db.run(action)
  }

  override def create(resource: DBResource): Future[Id] = {
    val query = ResourceTable.allDBResources
    val action = query returning query.map(_.id) += resource
    db.run(action)
  }

  override def update(id: Id, resource: DBResource): Future[Unit] = {
    val query = ResourceTable.byId(id)
    val action = query.update(resource)
    db.run(action).map(_ => ())
  }

  override def delete(id: Id): Future[Unit] = {
    val query = ResourceTable.byId(id)
    val action = query.delete
    db.run(action).map(_ => ())
  }
}
