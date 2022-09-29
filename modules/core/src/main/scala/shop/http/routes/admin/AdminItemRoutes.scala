package shop.http.routes.admin

import cats.MonadThrow
import cats.syntax.flatMap.*
import io.circe.JsonObject
import io.circe.syntax.*
import org.http4s.AuthedRoutes
import org.http4s.circe.CirceEntityEncoder.*
import org.http4s.circe.{JsonDecoder, *}
import org.http4s.dsl.Http4sDsl
import shop.domain.item.{CreateItemParam, UpdateItemParam}
import shop.http.auth.users.AdminUser
import shop.services.Items

final case class AdminItemRoutes[F[_]: JsonDecoder: MonadThrow](items: Items[F]) extends Http4sDsl[F] {

  private[admin] val prefixPath = "/items"

  private val httpRoutes: AuthedRoutes[AdminUser, F] =
    AuthedRoutes.of {
      case authedRequest @ POST -> Root as _ =>
        authedRequest.req.asJsonDecode[CreateItemParam].flatMap { item =>
          items.create(item.toDomain).flatMap { id =>
            Created(JsonObject.singleton("item_id", id.asJson))
          }
        }

      case authedRequest @ PUT -> Root as _ =>
        authedRequest.req.asJsonDecode[UpdateItemParam].flatMap { item =>
          items.update(item.toDomain) >> Ok()
        }

    }

}
