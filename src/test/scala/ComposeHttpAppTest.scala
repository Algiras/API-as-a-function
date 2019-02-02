import other.Base._
import org.specs2.concurrent.ExecutionEnv
import org.specs2.mutable.Specification

class ComposeHttpAppTest(implicit ee: ExecutionEnv) extends Specification {
  "Compose HttpApp" >> {
    "via middleware" >> {
      val text = "Bob"
      ComposeHttpApp.app(Request(POST, Uri("/hello"), text)) must ===(Response(OK, "Hello " + text + "!")).await
    }
  }
}
