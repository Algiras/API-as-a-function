import other.Base._
import org.specs2.concurrent.ExecutionEnv
import org.specs2.mutable.Specification

class HandlerUsingIOTest(implicit ee: ExecutionEnv) extends Specification {
  "Introduce IO" >> {
    val text = "Bob"
    val response = Response(OK, body = "Hello " + text + "!")

    "logs messages" >> {
      HandlerUsingIO.app(Request(POST, Uri("/hello"), body = text)).unsafeToFuture must ===(Response(OK, body = "Hello " + text)).await
      HandlerUsingIO.app(Request(POST, Uri("/holla"), body = text)).unsafeToFuture must ===(response).await
    }
  }
}
