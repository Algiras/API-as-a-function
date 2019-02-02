import other.Base._
import org.specs2.concurrent.ExecutionEnv
import org.specs2.mutable.Specification

class IOServiceTest(implicit ee: ExecutionEnv) extends Specification {
  "Introduce IO" >> {
    val text = "Bob"
    val response = Response(OK, "Hello " + text + "!")

    "logs messages" >> {
      IOService.app(Request(POST, Uri("/hello"), text)).unsafeToFuture must ===(Response(OK, "Hello " + text)).await
      IOService.app(Request(POST, Uri("/holla"), text)).unsafeToFuture must ===(response).await
    }
  }
}
