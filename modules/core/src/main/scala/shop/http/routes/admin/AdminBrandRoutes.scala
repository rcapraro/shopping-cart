package shop.http.routes.admin

import cats.MonadThrow
import cats.syntax.flatMap.*
import io.circe.JsonObject
import io.circe.syntax.*
import org.http4s.circe.CirceEntityEncoder.*
import org.http4s.circe.{JsonDecoder, *}
import org.http4s.dsl.Http4sDsl
import org.http4s.server.{AuthMiddleware, Router}
import org.http4s.{AuthedRoutes, HttpRoutes}
import shop.domain.brand.BrandParam
import shop.domain.brand.BrandParam.given
import shop.http.auth.users.AdminUser
import shop.services.Brands

final case class AdminBrandRoutes[F[_]: JsonDecoder: MonadThrow](brands: Brands[F]) extends Http4sDsl[F] {

  private[admin] val prefixPath = "/brands"

  private val httpRoutes: AuthedRoutes[AdminUser, F] =
    AuthedRoutes.of { case authedRequest @ POST -> Root as _ =>
      authedRequest.req.asJsonDecode[BrandParam].flatMap { bp =>
        brands.create(bp.toDomain).flatMap { id =>
          Created(JsonObject.singleton("brand_id", id.asJson))
        }
      }
    }

  def routes(authMiddleware: AuthMiddleware[F, AdminUser]): HttpRoutes[F] = Router(prefixPath -> authMiddleware(httpRoutes))
}
