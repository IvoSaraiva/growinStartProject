package growinupscala.mappings

import java.time.Instant

import growinupscala.Id
import growinupscala.model.DBResource
import slick.jdbc.H2Profile.api._
import slick.lifted.ProvenShape

class ResourceTable(tag: Tag) extends Table[DBResource](tag, "resource_table") {

  val id = column[String]("id", O.PrimaryKey)
  val col1 = column[Option[String]]("col1")
  val col2 = column[Long]("col2")
  val col3 = column[Instant]("col3")

  def * : ProvenShape[DBResource] = (id, col1, col2, col3) <> (DBResource.tupled, DBResource.unapply)
}

object ResourceTable {
  val allDBResources = TableQuery[ResourceTable]
  def byId(id: Id): Query[ResourceTable, DBResource, Seq] = allDBResources.filter(_.id === id)
}
