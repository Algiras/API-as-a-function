import other.Base._
import other.Translator

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object Combine {
  type HttpHandler = Request => Future[Response]
  type HttpRoutes = Request => Option[Future[Response]]

  object HttpRoutes {
    def of(pf: PartialFunction[Request, Future[Response]]): HttpRoutes = pf.lift
  }

  def combine(x: HttpRoutes, y: HttpRoutes): HttpRoutes = req => x(req).orElse(y(req))

  def seal(routes: HttpRoutes): HttpHandler = routes.andThen(_.getOrElse(Future.successful(Response(NotFound))))

  val translate: HttpRoutes = HttpRoutes.of {
    case Request(POST, Uri("/translate"), _, text) =>
      Translator.future(text).map(res => Response(OK, body = res))
  }

  val hello: HttpRoutes = HttpRoutes.of {
    case Request(POST, Uri("/hello"), _, text) =>
      Future.successful(Response(OK, body = s"Hello $text"))
  }

  val app: HttpHandler = seal(combine(translate, hello))

}
