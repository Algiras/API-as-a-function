import other.Base._
import org.specs2.mutable.Specification
import org.specs2.concurrent.ExecutionEnv
import other.Authorization

class AsyncRequestTest(implicit ee: ExecutionEnv) extends Specification {
  "Async Request" >> {
    "response with NotFound on non matching route" >> {
      AsyncRequest.translate(Request(POST, Uri("/empty"))) must ===(Response(NotFound)).await
    }

    "hello service works" >> {
      val text = "Some text"
      val headers = Map("Authorization" -> Authorization.encoder("user", "password"))
      AsyncRequest.translate(Request(POST, Uri("/hello"), headers, text)) must ===(Response(OK, body = s"hello user. $text")).await
      AsyncRequest.translate(Request(POST, Uri("/hello"), body = text)) must ===(Response(Unauthorized)).await
    }
  }
}
