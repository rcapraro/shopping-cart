package shop.domain

import cats.derived.*
import cats.{Eq, Show}
import io.circe.Codec

import java.util.UUID

object category {

  case class CategoryId(value: UUID) derives Codec.AsObject, Show, Eq

  case class CategoryName(value: String) derives Codec.AsObject, Show, Eq

  case class CategoryParam(value: String) {
    def toDomain: CategoryName = CategoryName(value.toLowerCase.capitalize)
  }

  case class Category(uuid: CategoryId, name: CategoryName) derives Codec.AsObject, Show, Eq
}
