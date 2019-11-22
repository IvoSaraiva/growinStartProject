package growinupscala.api

import akka.{Done, NotUsed}
import com.lightbend.lagom.scaladsl.api.transport.Method
import com.lightbend.lagom.scaladsl.api.{Descriptor, Service, ServiceCall}
import growinupscala.Id
import growinupscala.model.{Resource, ResourceA}
import play.api.libs.json.JsValue

trait LagomResourceService extends Service {

  val res: Resource = ResourceA

  def getCall(id: Id): ServiceCall[NotUsed, String]

  def postCall: ServiceCall[JsValue, Done]

  def putCall(id: Id): ServiceCall[JsValue, Done]

  def deleteCall(id: Id): ServiceCall[NotUsed, Done]

  override final def descriptor: Descriptor = {
    import Service._
    named("lagomResourceService")
      .withCalls(
        restCall(Method.GET, s"/${res.name}/:id", getCall _),
        restCall(Method.POST, s"/${res.name}", postCall),
        restCall(Method.PUT, s"/${res.name}/:id", putCall _),
        restCall(Method.DELETE, s"/${res.name}/:id", deleteCall _),
      )
      .withAutoAcl(true)
  }
}