import other.Base._
import cats.Monad
import cats.data.{Kleisli, OptionT}
import cats.effect.{IO, Sync}
import cats.implicits._
import monix.eval.Task
import other.Translator

import scala.language.higherKinds

object GeneralizeService {
  type Http[F[_]] = Kleisli[F, Request, Response]
  type HttpApp[F[_]] = Http[F]
  object HttpApp {
    def apply[F[_]](f: Request => F[Response]): HttpApp[F] = new HttpApp[F](f)
  }

  def translate[F[_]: Monad](app: Http[F]): Http[F] = for {
    resp <- app
    tx <- Kleisli.liftF(Translator.translate[F](resp.body))
  } yield resp.copy(body = tx)

  def hello[F[_]: Sync](theUri: Uri): HttpRoutes[F] = HttpRoutes.of[F] {
    case Request(POST, uri, name) if uri == theUri =>
      Response(OK, s"Hello $name").pure[F]
  }

  type IOOption[A] = OptionT[IO, A]
  object IOOption  {
    def apply[A](value: IO[Option[A]]): IOOption[A] = new OptionT[IO, A](value)
  }

  type HttpRoutes[F[_]] = Http[OptionT[F, ?]]
  object HttpRoutes {
    def of[F[_]: Sync](pf: PartialFunction[Request, F[Response]]): HttpRoutes[F] = {
      Kleisli((req: Request) => OptionT(pf.lift(req).sequence))
    }
  }

  def seal[F[_]: Sync](routes: HttpRoutes[F]): HttpApp[F] = HttpApp[F]((req: Request) => routes(req).fold(Response(NotFound))(identity))


  def log[F[_]: Sync](app: Http[F]): Http[F] = {
    app.mapF(Sync[F].delay(println("Translating!")) *> _)
  }

  def en[F[_]: Sync]: HttpRoutes[F] = hello[F](Uri("/hello"))
  def es[F[_]: Sync]: HttpRoutes[F] = translate(hello(Uri("/holla")))

  val appIO: HttpApp[IO] = seal(en[IO].combineK(log(es[IO])))
  val appTask: HttpApp[Task] = seal(en[Task].combineK(log(es[Task])))
}
