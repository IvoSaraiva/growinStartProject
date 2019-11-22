package growinupscala.controllers

import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.headers.RawHeader
import akka.http.scaladsl.server.Directives.{delete => delete_, get => get_, post => post_, put => put_, _}
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import growinupscala.services.ResourceService
import growinupscala.{Id, JSON}
import spray.json.{DefaultJsonProtocol, JsObject}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

case class ResourceController(resource: String)(implicit ec: ExecutionContext)
  extends Controller[Future[HttpResponse]]
  with SprayJsonSupport
  with DefaultJsonProtocol {

  val route: Route =
    path(resource / Segment) { id =>
      get_ {
        onComplete(get(id)){
          case Success(response) => complete(response)
          case Failure(_) => complete(StatusCodes.ServiceUnavailable)
        }
      } ~
      put_ {
        entity(as[JsObject]) { json =>
          onComplete(put(id, json.toString)) {
            case Success(response) => complete(response)
            case Failure(_) => complete(StatusCodes.ServiceUnavailable)
          }
        }
      } ~
      delete_ {
        onComplete(delete(id)) {
          case Success(response) => complete(response)
          case Failure(_) => complete(StatusCodes.ServiceUnavailable)
        }
      }
    }  ~
    path(resource) {
      post_ {
        entity(as[JsObject]) { json =>
          onComplete(post(json.toString)) {
            case Success(response) => complete(response)
            case Failure(_) => complete(StatusCodes.ServiceUnavailable)
          }
        }
      }
    }

  override def get(id: Id): Future[HttpResponse] =
    ResourceService(resource).read(id)
      .map(_.name)
      .map(resource => HttpResponse(StatusCodes.OK, entity = HttpEntity(ContentTypes.`application/json`, resource)))

  override def post(resource: JSON): Future[HttpResponse] =
    ResourceService(resource).create(resource.toString)
      .map(id => HttpResponse(StatusCodes.Created).withHeaders(RawHeader("Location", s"/$resource/$id")))

  override def put(id: Id, resource: JSON): Future[HttpResponse] =
    ResourceService(resource).update(id, resource.toString)
      .map(_ => HttpResponse(StatusCodes.NoContent))

  override def delete(id: Id): Future[HttpResponse] =
    ResourceService(resource).delete(id)
      .map(_ => HttpResponse(StatusCodes.NoContent))
}
