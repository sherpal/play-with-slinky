package frontend

import slinky.core._
import slinky.core.annotations.react
import slinky.web.html._
import fr.hmil.roshttp.{HttpRequest, Method}
import monix.execution.Scheduler.Implicits.global

import scala.util.{Failure, Random, Success}
import fr.hmil.roshttp.response.SimpleHttpResponse

@react final class MakeCallButton extends Component {

  type Props = Unit
  type State = String

  def initialState: String = "empty"

  def makeRequest(): Unit = {
    val request = HttpRequest(s"http://localhost:8080/play/hello/${Random.nextInt()}")

    request
      .withMethod(Method.POST)
      .send()
      .onComplete({
        case res: Success[SimpleHttpResponse] => setState(res.get.body)
        case e: Failure[SimpleHttpResponse]   => setState(e.exception.getMessage)
      })
  }

  def render() = p(
    button(
      onClick := (() => makeRequest())
    )("Click me!"),
    "response: ",
    state
  )

}
