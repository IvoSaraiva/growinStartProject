package growinupscala.controllers

import com.google.inject.Inject
import growinupscala.model.ResourceB
import play.api.mvc.ControllerComponents

import scala.concurrent.ExecutionContext

case class ResourceControllerB @Inject() (cc: ControllerComponents)(implicit ec: ExecutionContext)
  extends ControllerImpl(cc, ResourceB)
