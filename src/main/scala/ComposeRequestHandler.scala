import Combine.{hello, seal, HttpHandler}
import other.Translator
import scala.concurrent.ExecutionContext.Implicits.global

object ComposeRequestHandler {
  def translate(app: HttpHandler): HttpHandler = {
    app.andThen(future => for {
      resp <- future
      tx <- Translator.future(resp.body)
    } yield resp.copy(body = tx))
  }

  val app: HttpHandler = translate(seal(hello))
}
