package frontend.forms

import akka.stream.QueueOfferResult
import slinky.core._
import slinky.core.annotations.react
import slinky.core.facade.ReactElement
import slinky.web.html._

import scala.concurrent.Future

@react final class InputForm extends StatelessComponent {

  case class Props(
      errorKey: String,
      changeFormData: String => Future[QueueOfferResult],
      errors: List[FormError]
  )

  override def render(): ReactElement = div(
    props.errorKey,
    input(onChange := (event => props.changeFormData(event.target.value))),
    props.errors.filter(_.errorKey == props.errorKey).map(_.display)
  )

}
