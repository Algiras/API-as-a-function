import cats.effect.{IO, Sync}
import other.Base._
import cats.effect.Sync._
import org.specs2.concurrent.ExecutionEnv
import org.specs2.mutable.Specification
import monix.execution.Scheduler.Implicits.global
import cats.implicits._
import monix.eval.Task

class GeneralizeHandlerTest(implicit ee: ExecutionEnv) extends Specification {
  "Generalized Service" >> {
    val text = "Bob"
    val response = Response(OK, body = "Hello " + text + "!")

    "IO" >> {
      val serv = GeneralizeHandler.app[IO]
      serv(Request(POST, Uri("/hello"), body = text)).unsafeToFuture must ===(Response(OK, body = "Hello " + text)).await
      serv(Request(POST, Uri("/holla"), body = text)).unsafeToFuture must ===(response).await
    }

    "Task" >> {
      val serv = GeneralizeHandler.app[Task]
      serv(Request(POST, Uri("/hello"), body = text)).runToFuture must ===(Response(OK, body = "Hello " + text)).await
      serv(Request(POST, Uri("/holla"), body = text)).runToFuture must ===(response).await
    }
  }
}
