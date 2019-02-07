package other

import java.nio.charset.StandardCharsets
import java.util.Base64

import org.specs2.mutable.Specification
import Authorization._

class AuthorizationTest extends Specification {
  "Authorization" >> {
    "encoding a User should result in a valid user" >> {
      val user@User(username, password) = User("name", "password")
      decoder(encoder(username, password)) must beSome(user)
    }

    "invalid user encoding should fail" >> {
      def prepareString(string: String) = Base64.getEncoder.encodeToString(s"username".getBytes(StandardCharsets.UTF_8))

      decoder("") must beNone
      decoder(prepareString(s"username")) must beNone
      decoder(prepareString(s":password")) must beNone
    }
  }
}
