package shop.http.routes

import cats.Monad
import org.http4s.circe.CirceEntityCodec.*
import org.http4s.dsl.Http4sDsl
import org.http4s.server.Router
import org.http4s.{HttpRoutes, ParseFailure, QueryParamDecoder}
import shop.domain.brand.BrandParam
import shop.services.Items

given brandQueryParamDecoder: QueryParamDecoder[BrandParam] =
  QueryParamDecoder[String].emap(brand => Either.cond(brand.isEmpty, BrandParam(brand), ParseFailure("Empty brand not allowed", "")))

final case class ItemRoutes[F[_]: Monad](items: Items[F]) extends Http4sDsl[F] {

  private[routes] val prefixPath = "/items"

  object BrandQueryParam extends OptionalQueryParamDecoderMatcher[BrandParam]("brand")

  private val httpRoutes: HttpRoutes[F] = HttpRoutes.of[F] { case GET -> Root :? BrandQueryParam(brand) =>
    Ok(brand.fold(items.findAll)(b => items.findBy(b.toDomain)))
  }

  val routes: HttpRoutes[F] = Router(prefixPath -> httpRoutes)

}
