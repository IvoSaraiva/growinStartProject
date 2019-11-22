package growinupscala

import cats.effect.{ConcurrentEffect, ContextShift, Effect, ExitCode, Resource, Timer}
import growinupscala.controllers.Http4sController
import growinupscala.model.{ResourceA, ResourceB}
import cats.implicits._
import org.http4s.HttpRoutes
import org.http4s.implicits._
import org.http4s.server.blaze.BlazeServerBuilder
import fs2.Stream

import scala.language.higherKinds

object GUSServer {

  def routes[F[_]: Effect]: HttpRoutes[F] = Http4sController(ResourceA).routes <+> Http4sController(ResourceB).routes

  def server[F[_]: ConcurrentEffect: Timer]: Stream[F, ExitCode] =
    BlazeServerBuilder[F]
      .bindHttp(8080, "localhost")
      .withHttpApp(routes[F].orNotFound)
      .serve

}
