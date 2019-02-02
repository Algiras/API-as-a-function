package other

object Base {
  case class Uri(uri: String)

  sealed trait Method
  case object GET extends Method
  case object POST extends Method
  case object PUT extends Method
  case object DELETE extends Method

  sealed trait Status
  case object NotFound extends Status
  case object OK extends Status


  case class Request(method: Method, uri: Uri, body: String = "")
  case class Response(status: Status, body: String = "")
}
