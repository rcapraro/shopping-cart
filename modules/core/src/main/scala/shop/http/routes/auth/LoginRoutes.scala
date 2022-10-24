package shop.http.routes.auth

import cats.MonadThrow
import cats.syntax.applicativeError.*
import cats.syntax.flatMap.*
import http4s.jwt.JwtToken
import io.circe.{Decoder, Encoder}
import org.http4s.HttpRoutes
import org.http4s.circe.CirceEntityEncoder.*
import org.http4s.circe.*
import org.http4s.dsl.Http4sDsl
import org.http4s.server.Router
import shop.domain.JwtToken.given
import shop.domain.auth.*
import shop.services.Auth

final case class LoginRoutes[F[_]: JsonDecoder: MonadThrow](auth: Auth[F]) extends Http4sDsl[F] {

  private[routes] val prefixPath = "/auth"

  private val httpRoutes: HttpRoutes[F] = HttpRoutes.of[F] { case req @ POST -> Root / "login" =>
    req
      .asJsonDecode[LoginUser]
      .flatMap { user =>
        auth
          .login(user.username.toDomain, user.password.toDomain)
          .flatMap(Ok(_))
          .recoverWith { case UserNotFound(_) | InvalidPassword(_) =>
            Forbidden()
          }
      }
  }

  val routes: HttpRoutes[F] = Router(prefixPath -> httpRoutes)

}
