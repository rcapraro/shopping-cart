package shop.domain

import shop.domain.auth.UserId
import shop.domain.item.{Item, ItemId}
import squants.Money
import squants.market.EUR

import scala.util.control.NoStackTrace

object cart {

  case class Quantity(value: Int) extends AnyVal

  case class Cart(items: Map[ItemId, Quantity])

  case class CartItem(item: Item, quantity: Quantity) {
    def subTotal: Money = EUR(item.price.amount * quantity.value)
  }

  case class CartTotal(items: List[CartItem], total: Money)

  case class CartNotFound(userId: UserId) extends NoStackTrace

}
