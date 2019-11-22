package growinupscala.impl

import com.lightbend.lagom.scaladsl.api.ServiceLocator
import com.lightbend.lagom.scaladsl.api.ServiceLocator.NoServiceLocator
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import com.lightbend.lagom.scaladsl.server._
import growinupscala.api.LagomResourceService
import play.api.libs.ws.ahc.AhcWSComponents

class LagomGusLoader extends LagomApplicationLoader {
  override def load(context: LagomApplicationContext): LagomApplication =
    new LagomgusApplication(context) {
      override def serviceLocator: ServiceLocator = NoServiceLocator
    }

  override def loadDevMode(context: LagomApplicationContext): LagomApplication =
    new LagomgusApplication(context) with LagomDevModeComponents

  override def describeService = Some(readDescriptor[LagomResourceService])
}

abstract class LagomgusApplication(context: LagomApplicationContext)
  extends LagomApplication(context)
  with LagomServerComponents
  with AhcWSComponents {

  override lazy val lagomServer: LagomServer = serverFor[LagomResourceService](LagomResourceServiceImpl())
}
