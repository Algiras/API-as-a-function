import other.Base._
import org.specs2.mutable.Specification

class FirstTryTest extends Specification {
  "Basics API" >> {
    "response with NotFound on non matching route" >> {
      FirstTry.helloWorld(Request(GET, Uri("/some-rul"))) must_=== Response(NotFound)
    }
    "hello service works" >> {
      val name = "Bob"
      FirstTry.helloWorld(Request(POST, Uri("/hello"), name)) must_=== Response(OK, "Hello, Bob!")
    }
  }
}
