import Combine.{hello, seal, HttpApp}
import other.Translator
import scala.concurrent.ExecutionContext.Implicits.global

object ComposeHttpApp {
  def translate(app: HttpApp): HttpApp = {
    app.andThen(future => for {
      resp <- future
      tx <- Translator.future(resp.body)
    } yield resp.copy(body = tx))
  }

  val app: HttpApp = translate(seal(hello))
}
