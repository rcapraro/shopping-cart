package shop.http.routes.secured

import cats.Monad
import cats.syntax.apply.*
import cats.syntax.flatMap.*
import cats.syntax.traverse.*
import org.http4s.circe.CirceEntityEncoder.*
import org.http4s.circe.*
import org.http4s.dsl.Http4sDsl
import org.http4s.server.{AuthMiddleware, Router}
import org.http4s.{AuthedRoutes, HttpRoutes}
import shop.domain.cart.Cart
import shop.domain.item.ItemId
import shop.http.auth.users.CommonUser
import shop.http.vars.ItemIdVar
import shop.services.ShoppingCart

final case class CartRoutes[F[_]: JsonDecoder: Monad](shoppingCart: ShoppingCart[F]) extends Http4sDsl[F] {

  private[routes] val prefixPath = "/cart"

  private val httpRoutes: AuthedRoutes[CommonUser, F] = AuthedRoutes.of {

    // Get shopping cart
    case GET -> Root as user => Ok(shoppingCart.get(user.value.id))

    // Add items to the cart
    case authedRequest @ POST -> Root as user =>
      authedRequest.req.asJsonDecode[Cart].flatMap {
        _.items
          .map { case (id, quantity) =>
            shoppingCart.add(user.value.id, id, quantity)
          }
          .toList
          .sequence *> Created()
      }

    // Modify items in the cart
    case authedRequest @ PUT -> Root as user =>
      authedRequest.req.asJsonDecode[Cart].flatMap {
        shoppingCart.update(user.value.id, _) *> Ok()
      }

    // Remote items from the cart
    case DELETE -> Root / ItemIdVar(itemId) as user =>
      shoppingCart.removeItem(user.value.id, itemId) *> NoContent()

  }

  def routes(authMiddleware: AuthMiddleware[F, CommonUser]): HttpRoutes[F] = Router(prefixPath -> authMiddleware(httpRoutes))
}
