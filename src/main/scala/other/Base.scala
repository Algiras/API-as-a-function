package other

object Base {
  case class Uri(uri: String)
  case class Headers(headers: Map[String, String])

  sealed trait Method
  case object GET extends Method
  case object POST extends Method
  case object PUT extends Method
  case object DELETE extends Method

  sealed trait Status
  case object NotFound extends Status
  case object OK extends Status
  case object Unauthorized extends Status

  case class Request(method: Method, uri: Uri, headers: Map[String, String] = Map.empty[String, String], body: String = "")
  case class Response(status: Status, headers: Map[String, String] = Map.empty[String, String], body: String = "")
}
