package growinupscala.repositories

import cats.effect.Effect
import doobie.util.transactor.Transactor
import doobie.implicits._
import cats.implicits._
import growinupscala.Id
import growinupscala.model.DBResource

import scala.language.higherKinds

case class DoobieRepository[F[_]: Effect](tx: Transactor[F]) extends BetterRepository[F, DBResource] {

  override def create(resource: DBResource): F[Id] =
    sql"INSERT INTO resource_table (name) VALUES (${resource.name});"
      .update
      .withUniqueGeneratedKeys[String]("id")
      .transact(tx)

  override def read(id: Id): F[DBResource] =
    sql"SELECT * FROM resource_table WHERE id = $id;"
      .query[DBResource]
      .unique
      .transact(tx)

  override def update(id: Id, resource: DBResource): F[Unit] =
    sql"UPDATE resource_table SET name = ${resource.name} WHERE id = $id;"
      .update
      .run
      .transact(tx)
      .map(_ => ())

  override def delete(id: Id): F[Unit] =
    sql"DELETE FROM resource_table WHERE id = $id;"
      .update
      .run
      .transact(tx)
      .map(_ => ())
}