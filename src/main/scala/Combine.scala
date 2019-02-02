import other.Base._
import other.Translator

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object Combine {
  type HttpApp = Request => Future[Response]
  type HttpRoutes = Request => Option[Future[Response]]

  object HttpRoutes {
    def of(pf: PartialFunction[Request, Future[Response]]): HttpRoutes = {
      pf.lift
    }
  }

  def combine(x: HttpRoutes, y: HttpRoutes): HttpRoutes = req => x(req).orElse(y(req))

  def seal(routes: HttpRoutes): HttpApp = routes.andThen(_.getOrElse(Future.successful(Response(NotFound))))

  val translate: HttpRoutes = HttpRoutes.of {
    case Request(POST, Uri("/translate"), text) =>
      Translator.future(text).map(Response(OK, _))
  }

  val hello: HttpRoutes = HttpRoutes.of {
    case Request(POST, Uri("/hello"), text) =>
      Future.successful(Response(OK, s"Hello $text"))
  }

  val app: HttpApp = seal(combine(translate, hello))

}
