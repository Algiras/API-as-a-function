import other.Base._
import org.specs2.concurrent.ExecutionEnv
import org.specs2.mutable.Specification

class CombineTest(implicit ee: ExecutionEnv) extends Specification {
  "Combine request" >> {
    "combining request makes them both work" >> {
      val text = "Bob"
      Combine.app(Request(POST, Uri("/translate"), body = text)) must ===(Response(OK, body = text + "!")).await
      Combine.app(Request(POST, Uri("/hello"), body = text)) must ===(Response(OK, body = "Hello " + text)).await
      Combine.app(Request(GET, Uri("/WAT"))) must ===(Response(NotFound)).await
    }
  }
}
