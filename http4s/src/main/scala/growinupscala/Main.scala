package growinupscala

import cats.{Apply, Functor}
import cats.effect.{ExitCode, IO, IOApp}
import cats.implicits._
import cats.data._

object Main extends IOApp {
  def run(args: List[String]): IO[ExitCode] =
    GUSServer.server[IO].compile.drain.as(ExitCode.Success)

}
