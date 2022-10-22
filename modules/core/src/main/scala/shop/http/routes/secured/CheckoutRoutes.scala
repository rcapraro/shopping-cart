package shop.http.routes.secured

import cats.MonadThrow
import cats.syntax.applicativeError.*
import cats.syntax.flatMap.*
import cats.syntax.show.*
import org.http4s.circe.CirceEntityEncoder.*
import org.http4s.circe.*
import org.http4s.dsl.Http4sDsl
import org.http4s.server.{AuthMiddleware, Router}
import org.http4s.{AuthedRoutes, HttpRoutes}
import shop.domain.cart.CartNotFound
import shop.domain.checkout.Card
import shop.domain.order.{EmptyCartError, OrderOrPaymentError}
import shop.http.auth.users.CommonUser
import shop.programs.Checkout

final case class CheckoutRoutes[F[_]: JsonDecoder: MonadThrow](checkout: Checkout[F]) extends Http4sDsl[F] {

  private[routes] val prefixPath = "/checkout"

  private val httpRoutes: AuthedRoutes[CommonUser, F] = AuthedRoutes.of { case authedRequest @ POST -> Root as user =>
    authedRequest.req.asJsonDecode[Card].flatMap { card =>
      checkout
        .process(user.value.id, card)
        .flatMap(Created(_))
        .recoverWith {
          case CartNotFound(userId) =>
            NotFound(s"Cart not found for user: ${userId.value}")
          case EmptyCartError =>
            BadRequest("Shopping cart is empty!")
          case e: OrderOrPaymentError =>
            BadRequest(e.show)
        }
    }
  }

  def routes(authMiddleware: AuthMiddleware[F, CommonUser]): HttpRoutes[F] = Router(prefixPath -> authMiddleware(httpRoutes))

}
