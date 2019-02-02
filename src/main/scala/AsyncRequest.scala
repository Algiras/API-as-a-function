import other.Base._
import other.Translator
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object AsyncRequest {
  type HttpApp = Request => Future[Response]
  val translate: HttpApp = {
    case Request(POST, Uri("/translate"), text) =>
      Translator.future(text).map(Response(OK, _))
    case _ => Future.successful(Response(NotFound))
  }
}
