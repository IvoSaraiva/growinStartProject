package growinupscala.controllers

import cats.Applicative
import cats.effect._
import cats.implicits._
import growinupscala.services.BetterResourceService
import org.http4s._
import org.http4s.dsl.Http4sDsl
import growinupscala.{Id, JSON, model}

import scala.language.higherKinds

case class Http4sController[F[_]: Effect](res: model.Resource) extends Controller[F[_]] with Http4sDsl[F] {
  import Http4sController._

  override def get(id: Id): F[model.Resource] = BetterResourceService[F](res).read(id)

  override def post(resource: JSON): F[Id] = BetterResourceService[F](res).create(resource)

  override def put(id: Id, resource: JSON): F[Unit] = BetterResourceService[F](res).update(id, resource)

  override def delete(id: Id): F[Unit] = BetterResourceService[F](res).delete(id)

  private val Name = res.name.toLowerCase

  val routes: HttpRoutes[F] = HttpRoutes.of[F] {
    case GET -> Root / Name / id =>
      for {
        res <- get(id)
        response <- Ok(res)
      } yield response

    case req @ POST -> Root / Name =>
      for {
        body <- req.as[JSON]
        id <- post(body)
        response <- Created(Header("Location", s"$Name/$id"))
      } yield response

    case req @ PUT -> Root / Name / id =>
      for {
        body <- req.as[JSON]
        _ <- put(id, body)
        response <- NoContent()
      } yield response

    case DELETE -> Root / Name / id =>
      for {
        _ <- delete(id)
        response <- NoContent()
      } yield response
  }
}

object Http4sController {

  implicit def resourceEncoder[F[_]: Applicative] : EntityEncoder[F, model.Resource] =
    EntityEncoder.stringEncoder[F].contramap[model.Resource](_.name)

}
