package growinupscala

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import akka.stream.ActorMaterializer
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import growinupscala.controllers.ResourceController
import growinupscala.model.{ResourceA, ResourceB}

import scala.concurrent.{ExecutionContext, Future}

object Server extends App {

  implicit val system: ActorSystem = ActorSystem("growinUpScalaServer")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContext = system.dispatcher

  val routes: Route = ResourceController(ResourceA.name).route ~ ResourceController(ResourceB.name).route

  val serverBinding: Future[ServerBinding] = Http().bindAndHandle(routes, "localhost", 8000)

  println(s"Server online at http://localhost:8000/\nPress RETURN to stop...")
  scala.io.StdIn.readLine() // let it run until user presses return
  serverBinding
    .flatMap(_.unbind())// trigger unbinding from the port
    .onComplete(_ => system.terminate())// and shutdown when done

}
