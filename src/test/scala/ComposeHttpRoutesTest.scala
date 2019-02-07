import other.Base._
import org.specs2.concurrent.ExecutionEnv
import org.specs2.mutable.Specification


class ComposeHttpRoutesTest(implicit ee: ExecutionEnv) extends Specification {
  "Compose HttpRoutes" >> {
    val text = "Bob"
    val response = Response(OK, body = "Hello " + text + "!")

    "via middleware" >> {
      ComposeHttpRoutes.app(Request(POST, Uri("/hello"), body = text)) must ===(response).await
      ComposeHttpRoutes.app2(Request(POST, Uri("/hello"), body = text)) must ===(response).await
      ComposeHttpRoutes.app(Request(POST, Uri("/hello!"), body = text)) must ===(Response(NotFound)).await
    }

    "supports composition" >> {
      ComposeHttpRoutes.app3(Request(POST, Uri("/hello"), body = text)) must ===(Response(OK, body = "Hello " + text)).await
      ComposeHttpRoutes.app3(Request(POST, Uri("/holla"), body = text)) must ===(response).await
    }

    "logs messages" >> {
      ComposeHttpRoutes.app4(Request(POST, Uri("/hello"), body = text)) must ===(Response(OK, body = "Hello " + text)).await
      ComposeHttpRoutes.app4(Request(POST, Uri("/holla"), body = text)) must ===(response).await
    }
  }
}
