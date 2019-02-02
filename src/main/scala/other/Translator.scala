package other

import cats.Monad
import cats.instances.future._
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.Future

object Translator {
  def future(text: String): Future[String] = translate[Future](text)
  def translate[F[_]: Monad](text: String): F[String] = Monad[F].pure(text + "!")
}
