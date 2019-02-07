import other.Base._

object FirstTry {
  type HttpHandler = Request => Response
  val helloWorld: HttpHandler = {
    case Request(POST, Uri("/hello"), _, name) =>
      Response(OK, body = s"Hello, $name!")
    case _ => Response(NotFound)
  }
}
