package frontend.forms

import slinky.core.facade.ReactElement
import slinky.web.html.{div, key, li, ul}

case class FormData(name: String, email: String) {
  def display: ReactElement = div(
    ul(
      li(key := "name")("Name: ", name),
      li(key := "email")("Email: ", email)
    )
  )
}

