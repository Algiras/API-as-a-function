import other.Base._
import org.specs2.concurrent.ExecutionEnv
import org.specs2.mutable.Specification
import monix.execution.Scheduler.Implicits.global

class GeneralizeServiceTest(implicit ee: ExecutionEnv) extends Specification {
  "Generalized Service" >> {
    val text = "Bob"
    val response = Response(OK, "Hello " + text + "!")

    "IO" >> {
      GeneralizeService.appIO(Request(POST, Uri("/hello"), text)).unsafeToFuture must ===(Response(OK, "Hello " + text)).await
      GeneralizeService.appIO(Request(POST, Uri("/holla"), text)).unsafeToFuture must ===(response).await
    }

    "Task" >> {
      GeneralizeService.appTask(Request(POST, Uri("/hello"), text)).runToFuture must ===(Response(OK, "Hello " + text)).await
      GeneralizeService.appTask(Request(POST, Uri("/holla"), text)).runToFuture must ===(response).await
    }
  }
}
