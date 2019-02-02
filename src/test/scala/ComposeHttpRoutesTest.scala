import other.Base._
import org.specs2.concurrent.ExecutionEnv
import org.specs2.mutable.Specification


class ComposeHttpRoutesTest(implicit ee: ExecutionEnv) extends Specification {
  "Compose HttpRoutes" >> {
    val text = "Bob"
    val response = Response(OK, "Hello " + text + "!")

    "via middleware" >> {
      ComposeHttpRoutes.app(Request(POST, Uri("/hello"), text)) must ===(response).await
      ComposeHttpRoutes.app2(Request(POST, Uri("/hello"), text)) must ===(response).await
      ComposeHttpRoutes.app(Request(POST, Uri("/hello!"), text)) must ===(Response(NotFound)).await
    }

    "supports composition" >> {
      ComposeHttpRoutes.app3(Request(POST, Uri("/hello"), text)) must ===(Response(OK, "Hello " + text)).await
      ComposeHttpRoutes.app3(Request(POST, Uri("/holla"), text)) must ===(response).await
    }

    "logs messages" >> {
      ComposeHttpRoutes.app4(Request(POST, Uri("/hello"), text)) must ===(Response(OK, "Hello " + text)).await
      ComposeHttpRoutes.app4(Request(POST, Uri("/holla"), text)) must ===(response).await
    }
  }
}
