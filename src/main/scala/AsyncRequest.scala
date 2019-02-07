import other.Authorization
import other.Base._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object AsyncRequest {
  type HttpHandler = Request => Future[Response]
  val translate: HttpHandler = {
    case Request(POST, Uri("/hello"), headers, text) =>
      Authorization.future(headers.get("Authorization")).map(username => {
        username.map(u => Response(OK, body = s"hello $u. $text")).getOrElse(Response(Unauthorized))
      })
    case _ => Future.successful(Response(NotFound))
  }
}
