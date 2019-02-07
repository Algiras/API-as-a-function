package other

import java.nio.charset.StandardCharsets
import java.util.Base64

import cats.Monad
import cats.instances.future._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object Authorization {
  val userDB = Map(
    "user" -> "password"
  )

  case class User(username: String, password: String)

  def future(headerValue: Option[String]): Future[Option[String]] = authenticate[Future](headerValue)
  def authenticate[F[_]: Monad](headerValue: Option[String]): F[Option[String]] = Monad[F].pure(headerValue.flatMap(decoder).flatMap(user => {
    userDB.get(user.username).flatMap(psw => if(user.password == psw) Some(user.username) else None)
  }))

  def encoder(username: String, password: String): String = Base64.getEncoder.encodeToString(s"$username:$password".getBytes(StandardCharsets.UTF_8))
  def decoder(encodedUser: String): Option[User] = {
    val userString = new String(Base64.getDecoder.decode(encodedUser))

    userString.split(':').toList match {
      case username :: List(password) if !username.isEmpty && !password.isEmpty => Some(User(username, password))
      case _ => None
    }
  }
}
