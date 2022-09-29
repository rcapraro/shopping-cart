package shop.domain

import cats.derived.*
import cats.{Eq, Show}
import io.circe.{Codec, KeyDecoder, KeyEncoder}
import shop.Money.given
import shop.domain.brand.*
import shop.domain.cart.{CartItem, Quantity}
import shop.domain.category.*
import squants.market.*

import java.util.UUID
import scala.util.Try

object item {

  case class ItemId(value: UUID) derives Codec.AsObject, Show, Eq
  object ItemId {
    given keyEncoder: KeyEncoder[ItemId] = (itemId: ItemId) => itemId.value.toString
    given keyDecoder: KeyDecoder[ItemId] = (key: String) => Try(ItemId(UUID.fromString(key))).toOption
  }

  case class ItemName(value: String) derives Codec.AsObject, Show, Eq

  case class ItemDescription(value: String) derives Codec.AsObject, Show, Eq

  case class Item(uuid: ItemId, name: ItemName, description: ItemDescription, price: Money, brand: Brand, category: Category) derives Codec.AsObject, Show, Eq

  case class ItemNameParam(value: String) derives Codec.AsObject, Show

  case class ItemDescriptionParam(value: String) derives Codec.AsObject, Show

  case class PriceParam(value: String) derives Codec.AsObject, Show

  case class CreateItemParam(name: ItemNameParam, description: ItemDescriptionParam, price: PriceParam, brandId: BrandId, categoryId: CategoryId)
      derives Codec.AsObject,
        Show {
    def toDomain: CreateItem =
      CreateItem(ItemName(name.value), ItemDescription(description.value), USD(BigDecimal(price.value)), brandId, categoryId)
  }

  case class CreateItem(name: ItemName, description: ItemDescription, price: Money, brandId: BrandId, categoryId: CategoryId)

  case class ItemIdParam(value: String) derives Codec.AsObject

  case class UpdateItemParam(id: ItemIdParam, price: PriceParam) derives Codec.AsObject {
    def toDomain: UpdateItem =
      UpdateItem(ItemId(UUID.fromString(id.value)), EUR(BigDecimal(price.value)))
  }

  case class UpdateItem(id: ItemId, price: Money) derives Codec.AsObject

}
