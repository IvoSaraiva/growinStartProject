package growinupscala

import cats.effect._
import doobie._
import doobie.implicits._
import doobie.h2._
import growinupscala.model.DBResource
import growinupscala.repositories.DoobieRepository

object Main extends IOApp {

  val transactor: Resource[IO, H2Transactor[IO]] =
    for {
      ce <- ExecutionContexts.fixedThreadPool[IO](32) // our connect EC
      te <- ExecutionContexts.cachedThreadPool[IO]    // our transaction EC
      xa <- H2Transactor.newH2Transactor[IO](
        "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", // connect URL
        "sa",                                   // username
        "",                                     // password
        ce,                                     // await connection here
        te                                      // execute JDBC operations here
      )
    } yield xa

  override def run(args: List[String]): IO[ExitCode] =
    transactor.use{ tx =>

      val rep = DoobieRepository[IO](tx)

      for {
        _ <- sql"""
              CREATE TABLE resource_table (
                id   INT NOT NULL AUTO_INCREMENT,
                name   VARCHAR(255) NOT NULL,
                PRIMARY KEY (id)
              )
            """.update.run.transact(tx)
        _ <- rep.create(DBResource(0, "someName"))
        _ <- rep.create(DBResource(0, "someName"))
        _ <- rep.create(DBResource(0, "someName"))
        _ <- rep.create(DBResource(0, "someName"))
        id <- rep.create(DBResource(0, "someName"))
        res <- rep.read(id)
        _ <- IO(println(res))
      } yield ExitCode.Success
    }
}
