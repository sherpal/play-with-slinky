package frontend

import akka.actor.ActorSystem
import frontend.forms.FormComponent
import org.scalajs.dom
import org.scalajs.dom.History
import slinky.core._
import slinky.core.annotations.react
import slinky.core.facade.ReactElement
import slinky.reactrouter._
import slinky.web.html._

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@JSImport("resources/App.css", JSImport.Default)
@js.native
object AppCSS extends js.Object

@JSImport("resources/logo.svg", JSImport.Default)
@js.native
object ReactLogo extends js.Object

@react class App extends StatelessComponent {
  type Props = Unit

  private val css = AppCSS

  implicit val actorSystem: ActorSystem = ActorSystem("slinky")

  ReactRouterDOM

  val history: History = History.createBrowserHistory()
  println(history)

  def render(): ReactElement = Router(history)(
    Route(path = "/", component = Home, exact = true),
    Route(path = "/home", component = Home),
    Route(path = "/about", component = About)
  )
//  {
//    div(
//      div(className := "App")(
//        header(className := "App-header")(
//          img(src := ReactLogo.asInstanceOf[String], className := "App-logo", alt := "logo"),
//          h1(className := "App-title")("Welcome to React (with Scala.js!)")
//        ),
//        p(className := "App-intro")(
//          "To get started, edit ",
//          code("App.scala"),
//          " and save to reload."
//        ),
//        div(
//          "Example of using a button to make an http call to play:",
//          MakeCallButton()
//        )
//      ),
//      div(
//        style := js.Dynamic.literal(
//          marginTop = "30px"
//        )
//      )(
//        FormComponent(actorSystem)
//      )
//    )
//  }
}
