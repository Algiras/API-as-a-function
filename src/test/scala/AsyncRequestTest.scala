import other.Base._
import org.specs2.mutable.Specification
import org.specs2.concurrent.ExecutionEnv

class AsyncRequestTest(implicit ee: ExecutionEnv) extends Specification {
  "Async Request" >> {
    "response with NotFound on non matching route" >> {
      AsyncRequest.translate(Request(POST, Uri("/empty"))) must ===(Response(NotFound)).await
    }

    "hello service works" >> {
      val name = "Bob"
      AsyncRequest.translate(Request(POST, Uri("/translate"), name)) must ===(Response(OK, name + "!")).await
    }
  }
}
