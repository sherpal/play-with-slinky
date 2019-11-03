package frontend.forms

import akka.actor.ActorSystem
import akka.stream.scaladsl.{Keep, Sink, Source, SourceQueueWithComplete}
import akka.stream.{ActorMaterializer, Materializer, OverflowStrategy, QueueOfferResult}
import slinky.core._
import slinky.core.annotations.react
import slinky.core.facade.ReactElement
import slinky.web.html._

import scala.concurrent.duration._
import scala.concurrent.Future

@react final class FormComponent extends Component {

  case class Props(actorSystem: ActorSystem)

  implicit def system: ActorSystem = props.actorSystem
  implicit lazy val materializer: Materializer = ActorMaterializer()

  /** Type alias to describe a change in the form data */
  type FormDataChanger = FormData => FormData

  /** The state contains the form data.
    * This is only used for render function. Indeed, the actual state should only be contained in the
    * `scan` `formSource`.
    *
    */
  case class State(formData: FormData, errors: List[FormError])

  def initialState: State = State(formData = FormData("", ""), errors = Nil)

  /**
    * Creates the list of errors that the `formData` contain.
    */
  def validate(formData: FormData): List[FormError] =
    List(
      Some(formData.name)
        .filter(_.nonEmpty) // if it's empty, we don't check validation here
        .filter(_.length < 6)
        .map(_ => FormError("name", "Name is too short, at least 6 characters are required")),
      Some(formData.email)
        .filter(_.nonEmpty) // if it's empty, we don't check validation here
        .filter(!_.contains("@"))
        .map(_ => FormError("email", "Email should contain the `@` symbol."))
    ).flatten

  /**
    * This queue allows children to add their inputs to the form of this [[FormComponent]].
    *
    * The stream changes the state of this form in both ways:
    * - it keeps the form data up to date
    * - it keeps the list of errors up to date after validating.
    *
    * This [[FormComponent]] is responsible to giving to children the possibility to add elements to
    * the queue.
    */
  lazy val formSource: SourceQueueWithComplete[FormDataChanger] = Source
    .queue[FormDataChanger](Int.MaxValue, OverflowStrategy.dropNew)
    .scan(FormData("", "")) { case (form, changer) => changer(form) }
    .alsoTo(Sink.foreach(form => setState(_.copy(formData = form))))
    .groupedWithin(10, 200.milliseconds)
    .map(_.last)
    .map(validate)
    .toMat(Sink.foreach(errors => setState(_.copy(errors = errors))))(Keep.left)
    .run()

  /**
    * Handlers given to the [[InputForm]]s children so that they can modify the form data.
    */
  val changeName: String => Future[QueueOfferResult] = newName => formSource.offer(_.copy(name = newName))
  val changeEmail: String => Future[QueueOfferResult] = newEmail => formSource.offer(_.copy(email = newEmail))

  def render(): ReactElement = div(
    state.formData.display,
    InputForm("name", changeName, state.errors),
    InputForm("email", changeEmail, state.errors)
  )
}
