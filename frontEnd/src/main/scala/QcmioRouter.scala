package org.qcmio.front

import com.raquo.laminar.api.L._
import com.raquo.laminar.nodes.ReactiveHtmlElement
import com.raquo.waypoint._
import upickle.default._
import org.scalajs.dom.html.Div


object QcmioRouter {

  sealed trait Page
  case class MainPage(userId: Int) extends Page
  case object LoginPage extends Page

  implicit val UserPageRW: ReadWriter[MainPage] = macroRW
  implicit val rw: ReadWriter[Page] = macroRW

  val mainRoute = Route[MainPage, Int](
    encode = mainPage => mainPage.userId,
    decode = arg => MainPage(userId = arg),
    pattern = root / "main" / segment[Int] / endOfSegments
  )

  val loginRoute = Route.static(LoginPage, root / "login" / endOfSegments)

  val router = new Router[Page](
    routes = List(mainRoute, loginRoute),
    getPageTitle = _.toString, // mock page title (displayed in the browser tab next to favicon)
    serializePage = page => write(page)(rw), // serialize page data for storage in History API log
    deserializePage = pageStr => read(pageStr)(rw) // deserialize the above
  )(
    $popStateEvent = windowEvents.onPopState, // this is how Waypoint avoids an explicit dependency on Laminar
    owner = unsafeWindowOwner // this router will live as long as the window
  )

  val splitter = SplitRender[Page, HtmlElement](router.$currentPage)
    .collectSignal[MainPage] { _=> renderMainPage() }
    .collectStatic(LoginPage) { div("Login page") }

  def renderMainPage(): ReactiveHtmlElement[Div] = {
    div(
      "User page "
    )
  }

}
