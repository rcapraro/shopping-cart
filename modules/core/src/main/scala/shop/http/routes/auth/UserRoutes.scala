package shop.http.routes.auth

import cats.MonadThrow
import cats.MonadThrow.apply
import cats.syntax.applicativeError.*
import cats.syntax.flatMap.*
import cats.syntax.show.*
import org.http4s.HttpRoutes
import org.http4s.circe.CirceEntityEncoder.*
import org.http4s.circe.*
import org.http4s.dsl.Http4sDsl
import org.http4s.server.Router
import shop.domain.JwtToken.given
import shop.domain.auth.*
import shop.services.Auth

final case class UserRoutes[F[_]: JsonDecoder: MonadThrow](auth: Auth[F]) extends Http4sDsl[F] {

  private[routes] val prefixPath = "/auth"

  private val httpRoutes: HttpRoutes[F] = HttpRoutes.of[F] { case req @ POST -> Root / "users" =>
    req
      .asJsonDecode[CreateUser]
      .flatMap { user =>
        auth
          .newUser(user.username.toDomain, user.password.toDomain)
          .flatMap(Created(_))
          .recoverWith { case UserNameInUse(u) =>
            Conflict(u.show)
          }
      }
  }

  val routes: HttpRoutes[F] = Router(prefixPath -> httpRoutes)

}
