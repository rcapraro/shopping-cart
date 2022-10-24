package shop.domain

import cats.derived.*
import cats.{Eq, Show}
import io.circe.{Codec, Decoder, Encoder}
import shop.domain.Money.given
import shop.domain.auth.UserId
import shop.domain.item.ItemId.*
import shop.domain.item.{Item, ItemId}
import squants.market.{EUR, Money}

import scala.util.control.NoStackTrace

object cart {

  case class Quantity(value: Int) derives Codec.AsObject, Show, Eq

  case class Cart(items: Map[ItemId, Quantity]) derives Show, Eq
  object Cart {
    given jsonEncoder: Encoder[Cart] = Encoder.forProduct1("items")(_.items)
    given jsonDecoder: Decoder[Cart] = Decoder.forProduct1("items")(Cart.apply)
  }

  case class CartItem(item: Item, quantity: Quantity) derives Codec.AsObject, Show, Eq {
    def subTotal: Money = EUR(item.price.amount * quantity.value)
  }

  case class CartTotal(items: List[CartItem], total: Money) derives Codec.AsObject, Show, Eq

  case class CartNotFound(userId: UserId) extends NoStackTrace derives Codec.AsObject

}
