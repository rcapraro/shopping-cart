package shop.http.routes.auth

import cats.Monad
import cats.syntax.apply.*
import cats.syntax.foldable.*
import http4s.AuthHeaders
import org.http4s.dsl.Http4sDsl
import org.http4s.server.{AuthMiddleware, Router}
import org.http4s.{AuthedRoutes, HttpRoutes}
import shop.http.auth.users.CommonUser
import shop.services.Auth

final case class LogoutRoutes[F[_]: Monad](auth: Auth[F]) extends Http4sDsl[F] {
  private[routes] val prefixPath = "/auth"

  private val httpRoutes: AuthedRoutes[CommonUser, F] =
    AuthedRoutes.of { case authedRequest @ POST -> Root / "logout" as user =>
      AuthHeaders
        .getBearerToken(authedRequest.req)
        .traverse_(auth.logout(_, user.value.name)) *> NoContent()
    }

  def routes(authMiddleware: AuthMiddleware[F, CommonUser]): HttpRoutes[F] = Router(prefixPath -> authMiddleware(httpRoutes))

}
