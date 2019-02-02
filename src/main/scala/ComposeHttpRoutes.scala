import other.Base._
import cats._
import cats.data.{Kleisli, OptionT}
import cats.implicits._
import other.Translator

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.language.higherKinds

object ComposeHttpRoutes {
  type Http[F[_]] = Kleisli[F, Request, Response]
  type HttpApp = Http[Future]
  object HttpApp {
    def apply(f: Request => Future[Response]): HttpApp = new HttpApp(f)
  }

  def translate[F[_]: Monad](app: Http[F]): Http[F] = for {
    resp <- app
    tx <- Kleisli.liftF(Translator.translate[F](resp.body))
  } yield resp.copy(body = tx)

  def hello(theUri: Uri): HttpRoutes = HttpRoutes.of {
    case Request(POST, uri, name) if uri == theUri =>
      Future.successful(Response(OK, s"Hello $name"))
  }

  type FutureOption[A] = OptionT[Future, A]
  object FutureOption {
    def apply[A](value: Future[Option[A]]): FutureOption[A] = new OptionT[Future, A](value)
  }

  type HttpRoutes = Http[FutureOption]
  object HttpRoutes {
    def of(pf: PartialFunction[Request, Future[Response]]): HttpRoutes = {
      Kleisli((req: Request) => pf.lift(req) match {
        case Some(res) => OptionT[Future, Response](res.map(Option(_)))
        case None => new OptionT[Future, Response](Future.successful(None))
      })
    }
  }

  def seal(routes: HttpRoutes): HttpApp = HttpApp((req: Request) => routes(req).fold(Response(NotFound))(identity))

  def app = seal(translate(hello(Uri("/hello"))))

  def app2 = translate(seal(hello(Uri("/hello"))))


  val en: HttpRoutes = hello(Uri("/hello"))
  val es: HttpRoutes = translate(hello(Uri("/holla")))

  val app3: HttpApp = seal(en.combineK(es))

  def log[F[_]: Monad](app: Http[F]): Http[F] = {
    app.mapF(Monad[F].pure(println("Translating!")) *> _)
  }

  val app4: HttpApp = seal(en.combineK(log(es)))
}
