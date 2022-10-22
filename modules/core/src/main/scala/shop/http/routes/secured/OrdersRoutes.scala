package shop.http.routes.secured

import cats.Monad
import org.http4s.circe.CirceEntityEncoder.*
import org.http4s.dsl.Http4sDsl
import org.http4s.server.{AuthMiddleware, Router}
import org.http4s.{AuthedRoutes, HttpRoutes}
import shop.http.auth.users.CommonUser
import shop.http.vars.OrderIdVar
import shop.services.Orders

final case class OrdersRoutes[F[_]: Monad](orders: Orders[F]) extends Http4sDsl[F] {

  private[routes] val prefixPath = "/orders"

  private val httpRoutes: AuthedRoutes[CommonUser, F] = AuthedRoutes.of {

    case GET -> Root as user =>
      Ok(orders.findBy(user.value.id))

    case GET -> Root / OrderIdVar(orderId) as user =>
      Ok(orders.get(user.value.id, orderId))
  }

  def routes(authMiddleware: AuthMiddleware[F, CommonUser]): HttpRoutes[F] = Router(prefixPath -> authMiddleware(httpRoutes))

}
