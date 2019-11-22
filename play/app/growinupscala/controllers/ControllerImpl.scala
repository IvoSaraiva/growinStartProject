package growinupscala.controllers

import com.google.inject.Inject
import growinupscala.{Id, JSON}
import growinupscala.model.{Resource, ResourceA}
import growinupscala.services.ResourceService
import play.api.libs.json.JsValue
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents, Result}

import scala.concurrent.{ExecutionContext, Future}

abstract class ControllerImpl(cc: ControllerComponents, res: Resource)(implicit ec: ExecutionContext)
  extends AbstractController(cc)
  with Controller[Future[Result]] {

  def getAction(id: Id): Action[AnyContent] = Action.async(get(id))

  def postAction: Action[JsValue] = Action.async(parse.json){ request => post(request.body.toString) }

  def putAction(id: Id): Action[JsValue] = Action.async(parse.json){ request => put(id, request.body.toString) }

  def deleteAction(id: Id): Action[AnyContent] = Action.async(delete(id))

  override def get(id: Id): Future[Result] =
    ResourceService(res.name).read(id).map(resource => Ok(resource.name))

  override def post(resource: JSON): Future[Result] =
    ResourceService(res.name).create(resource)
      .map(id => Created("").withHeaders("Location" -> s"/${ResourceA.name}/$id"))

  override def put(id: Id, resource: JSON): Future[Result] =
    ResourceService(res.name).update(id, resource)
      .map(_ => NoContent)

  override def delete(id: Id): Future[Result] =
    ResourceService(res.name).delete(id)
      .map(_ => NoContent)
}
