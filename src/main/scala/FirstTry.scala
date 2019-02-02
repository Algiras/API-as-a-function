import other.Base._

object FirstTry {
  type HttpApp = Request => Response
  val helloWorld: HttpApp = {
    case Request(POST, Uri("/hello"), name) =>
      Response(OK, s"Hello, $name!")
    case _ => Response(NotFound)
  }
}
