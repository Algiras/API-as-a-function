import other.Base._
import cats.Monad
import cats.data.{Kleisli, OptionT}
import cats.effect.{IO, Sync}
import cats.effect.IO._
import cats.effect.Sync._
import cats.implicits._
import other.Translator
import scala.language.higherKinds

object IOService {
  type Http[F[_]] = Kleisli[F, Request, Response]
  type HttpApp = Http[IO]
  object HttpApp {
    def apply(f: Request => IO[Response]): HttpApp = new HttpApp(f)
  }

  def translate[F[_]: Monad](app: Http[F]): Http[F] = for {
    resp <- app
    tx <- Kleisli.liftF(Translator.translate[F](resp.body))
  } yield resp.copy(body = tx)

  def hello(theUri: Uri): HttpRoutes = HttpRoutes.of {
    case Request(POST, uri, name) if uri == theUri =>
      IO(Response(OK, s"Hello $name"))
  }

  type IOOption[A] = OptionT[IO, A]
  object IOOption  {
    def apply[A](value: IO[Option[A]]): IOOption[A] = new OptionT[IO, A](value)
  }

  type HttpRoutes = Http[IOOption]
  object HttpRoutes {
    def of(pf: PartialFunction[Request, IO[Response]]): HttpRoutes = {
      Kleisli((req: Request) => pf.lift(req) match {
        case Some(res) => OptionT[IO, Response](res.map(Option(_)))
        case None => new OptionT[IO, Response](IO(None))
      })
    }
  }

  def seal(routes: HttpRoutes): HttpApp = HttpApp((req: Request) => routes(req).fold(Response(NotFound))(identity))


  def log[F[_]: Sync](app: Http[F]): Http[F] = {
    app.mapF(Sync[F].delay(println("Translating!")) *> _)
  }

  val en: HttpRoutes = hello(Uri("/hello"))
  val es: HttpRoutes = translate(hello(Uri("/holla")))

  val app: HttpApp = seal(en.combineK(log(es)))

  }
