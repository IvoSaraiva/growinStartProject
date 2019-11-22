package growinupscala.controllers

import com.google.inject.Inject
import growinupscala.model.ResourceA
import play.api.mvc.ControllerComponents

import scala.concurrent.ExecutionContext

case class ResourceControllerA @Inject() (cc: ControllerComponents)(implicit ec: ExecutionContext)
  extends ControllerImpl(cc, ResourceA)
