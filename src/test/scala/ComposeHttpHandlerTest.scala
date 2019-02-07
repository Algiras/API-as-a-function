import other.Base._
import org.specs2.concurrent.ExecutionEnv
import org.specs2.mutable.Specification

class ComposeHttpHandlerTest(implicit ee: ExecutionEnv) extends Specification {
  "Compose HttpHandler" >> {
    "via middleware" >> {
      val text = "Bob"
      ComposeRequestHandler.app(Request(POST, Uri("/hello"), body = text)) must ===(Response(OK, body = "Hello " + text + "!")).await
    }
  }
}
