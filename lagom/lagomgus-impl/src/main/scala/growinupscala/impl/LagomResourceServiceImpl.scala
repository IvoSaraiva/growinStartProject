package growinupscala.impl

import akka.{Done, NotUsed}
import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.lightbend.lagom.scaladsl.api.transport.ResponseHeader
import com.lightbend.lagom.scaladsl.server.ServerServiceCall
import growinupscala.{Id, JSON}
import growinupscala.api.LagomResourceService
import growinupscala.controllers.Controller
import growinupscala.services.ResourceService
import play.api.libs.json.JsValue

import scala.concurrent.{ExecutionContext, Future}

case class LagomResourceServiceImpl(implicit ec: ExecutionContext)
  extends Controller[Future[_]]
  with LagomResourceService {

  override def get(id: Id): Future[String] = ResourceService(res.name).read(id).map(_.name)

  override def post(resource: JSON): Future[Id] = ResourceService(res.name).create(resource)

  override def put(id: Id, resource: JSON): Future[Unit] = ResourceService(res.name).update(id, resource)

  override def delete(id: Id): Future[Unit] = ResourceService(res.name).delete(id)

  def getCall(id: Id): ServiceCall[NotUsed, String] = ServerServiceCall { _ => get(id) }

  def postCall: ServiceCall[JsValue, Done] = ServerServiceCall { (_, json) =>

    post(json.toString).map{ id =>
      val response = ResponseHeader.Ok
        .withStatus(201)
        .withHeader("Location", s"/${res.name}/$id")
      (response, Done)
    }
  }

  def putCall(id: Id): ServiceCall[JsValue, Done] = ServerServiceCall { (_, json) =>
    put(id, json.toString).map{ _ =>
      (ResponseHeader.Ok.withStatus(204), Done)
    }
  }

  def deleteCall(id: Id): ServiceCall[NotUsed, Done] = ServerServiceCall { (_, _) =>
    delete(id).map(_ => (ResponseHeader.Ok.withStatus(204), Done))
  }
}